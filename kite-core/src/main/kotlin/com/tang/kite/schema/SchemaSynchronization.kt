package com.tang.kite.schema

import com.tang.kite.annotation.Table
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.IndexMeta
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.AlterOperation
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.ddl.SqlStatementDdlHandler
import com.tang.kite.sql.datatype.DataType
import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Schema initializer for automatic database schema management based on entity classes.
 *
 * @author Tang
 */
class SchemaSynchronization(private val databaseValue: DatabaseValue) {

    private val logger = getLogger

    private val ddlExecutor = DdlExecutor(databaseValue)

    /**
     * Synchronize schema by scanning packages and synchronizing table structure
     */
    fun synchronizeSchema() {
        logger.info("Synchronizing database schema for database '${databaseValue.sqlDialect.getType().name}'")
        val entityClasses = SchemaScanner.scanEntityClasses(SchemaConfig.scanPackages)
        logger.info("Found ${entityClasses.size} entity classes")
        if (entityClasses.isEmpty()) {
            logger.warn("No entity classes found, schema initialization skipped")
            return
        }
        synchronizeTables(entityClasses)
        logger.info("Schema synchronization completed")
    }

    /**
     * Synchronize table structure by adding missing columns
     */
    private fun synchronizeTables(entityClasses: Set<KClass<*>>) {
        entityClasses.forEach { entityClass ->
            val tableName = Reflects.getTableName(entityClass.java)
            val tableExists = MetaDataHandlers.tableExists(databaseValue, tableName)
            if (SchemaConfig.dropExistingTables) {
                dropTable(entityClass, tableName, tableExists)
            }
            if ((SchemaConfig.createMissingTables && tableExists.not()) || SchemaConfig.dropExistingTables) {
                createTable(entityClass, tableName, tableExists)
                return
            }
            logger.info("Synchronizing table '$tableName' for entity ${entityClass.simpleName}")
            synchronizeTable(entityClass, tableName)
            synchronizeColumns(entityClass, tableName)
            synchronizeIndexes(entityClass, tableName)
            logger.info("Successfully synchronized table '$tableName'")
        }
    }

    private fun <T> getMissing(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
        return expected.filter { expectedItem ->
            !existing.any { existingItem ->
                getKey(existingItem).equals(getKey(expectedItem), ignoreCase = true)
            }
        }
    }

    private fun <T> getExtra(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
        return existing.filter { existingItem ->
            !expected.any { expectedItem ->
                getKey(existingItem).equals(getKey(expectedItem), ignoreCase = true)
            }
        }
    }

    /**
     * Drop table for entity if exists
     */
    private fun dropTable(entityClass: KClass<*>, tableName: String, tableExists: Boolean) {
        runCatching {
            if (tableExists) {
                logger.info("Dropping table '$tableName' for entity ${entityClass.simpleName}")
                val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
                val dropTable = SqlNode.DropTable(table = tableReference)
                ddlExecutor.executeDdl(dropTable.getFirstSql(databaseValue.sqlDialect))
            }
        }.onFailure {
            logger.error("Failed to drop table: $tableName", it)
        }
    }

    /**
     * Create table for entity if not exists
     */
    private fun createTable(entityClass: KClass<*>, tableName: String, tableExists: Boolean) {
        if (SchemaConfig.dropExistingTables.not() && tableExists) {
            logger.info("Table '$tableName' already exists, skipping creation")
            return
        }
        logger.info("Creating table '$tableName' for entity ${entityClass.simpleName}")
        val createTableNode = SchemaBuilder.buildEntity(entityClass)
        ddlExecutor.executeDdlBatch(createTableNode.getSqlList(databaseValue.sqlDialect))
        logger.info("Successfully created table '$tableName'")
    }

    private fun synchronizeTable(entityClass: KClass<*>, tableName: String) {
        val tableAnnotation = entityClass.findAnnotation<Table>()
        val expectedComment = tableAnnotation?.comment
        val existingTable = MetaDataHandlers.getTable(databaseValue, tableName)
        val existingComment = existingTable?.comment
        if (existingComment == expectedComment) {
            return
        }
        logger.info("Updating table comment for '$tableName' from '${existingComment ?: ""}' to '$expectedComment'")
        val dialect = databaseValue.sqlDialect
        val sql = if (dialect.supportsCommentOnTable()) {
            if (expectedComment == null) {
                "drop comment on table $tableName"
            } else {
                "comment on table $tableName is '$expectedComment'"
            }
        } else {
            if (expectedComment == null) {
                "alter table $tableName comment ''"
            } else {
                "alter table $tableName comment '$expectedComment'"
            }
        }
        ddlExecutor.executeDdl(sql)
        logger.info("Successfully updated table comment for '$tableName'")
    }

    private fun synchronizeColumns(entityClass: KClass<*>, tableName: String) {
        val existingColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        val expectedColumns = SchemaBuilder.buildColumns(entityClass)
        existingColumns.forEach { existingColumn ->
            val expectedColumn = expectedColumns.firstOrNull { existingColumn.columnName.equals(it.columnName, ignoreCase = true) }
            if (expectedColumn != null) {
                synchronizeColumn(tableName, existingColumn, expectedColumn)
            }
        }
        val missingColumns = getMissing(existingColumns, expectedColumns) { it.columnName }
        val extraColumns = getExtra(existingColumns, expectedColumns) { it.columnName }

        if (missingColumns.isEmpty() && extraColumns.isEmpty()) {
            logger.info("Table '$tableName' is already synchronized, no changes needed")
        }
        if (missingColumns.isNotEmpty()) {
            if (SchemaConfig.createMissingColumns) {
                logger.info("Found ${missingColumns.size} missing columns in table '$tableName'")
                missingColumns.forEach { missingColumn ->
                    addColumnToTable(tableName, missingColumn, entityClass)
                }
            } else {
                logger.warn("Table '$tableName' has ${missingColumns.size} missing columns that are not defined in entity ${entityClass.simpleName}")
            }
        }
        if (extraColumns.isNotEmpty()) {
            if (SchemaConfig.dropExistingColumns) {
                logger.info("Found ${extraColumns.size} extra columns in table '$tableName'")
                extraColumns.forEach { extraColumn ->
                    dropColumnFromTable(tableName, extraColumn, entityClass)
                }
            } else {
                logger.warn("Table '$tableName' has ${extraColumns.size} extra columns that are not defined in entity ${entityClass.simpleName}")
            }
        }
    }

    private fun synchronizeColumn(tableName: String, existingColumn: ColumnMeta, expectedColumn: ColumnMeta) {
        if (existingColumn == expectedColumn) {
            return
        }
        if ((existingColumn.comment == expectedColumn.comment).not()) {
            val dialect = databaseValue.sqlDialect
            val sql = SqlStatementDdlHandler.getColumnComment(tableName, expectedColumn, dialect)
            ddlExecutor.executeDdl(sql)
            logger.info("Successfully updated column comment for '${existingColumn.columnName}' in table '$tableName'")
        }
        val expectedColumnTypeName = DataType.normalize(expectedColumn.typeName)
        val existingColumnTypeName = DataType.normalize(existingColumn.typeName)
        if ((existingColumnTypeName == expectedColumnTypeName).not()) {
            logger.info("Column '${existingColumn.columnName}' type changed from '${existingColumn.typeName}' to '${expectedColumn.typeName}'")
            val alterTable = SqlNode.AlterTable(
                table = TableReference(tableName),
                operations = mutableListOf(AlterOperation.ModifyColumn(expectedColumn))
            )
            ddlExecutor.executeDdlBatch(alterTable.getSqlList(databaseValue.sqlDialect))
            logger.info("Successfully updated column type for '${existingColumn.columnName}' in table '$tableName'")
        }
    }

    private fun synchronizeIndexes(entityClass: KClass<*>, tableName: String) {
        val existingIndexes = MetaDataHandlers.getIndexes(databaseValue, tableName)
            .map { it.value }
            .filter { it.isPrimaryKey.not() }
        val expectedIndexes = SchemaBuilder.buildIndexes(entityClass)
        val synchronizationIndexes = expectedIndexes.filter { expectedIndex ->
            val existingIndex = existingIndexes.firstOrNull { expectedIndex.indexName.equals(it.indexName, ignoreCase = true) }
            if (existingIndex != null) {
                (existingIndex.columns == expectedIndex.columns
                && existingIndex.sorts == expectedIndex.sorts
                && existingIndex.unique == expectedIndex.unique
                && existingIndex.filterCondition == expectedIndex.filterCondition).not()
            } else {
                false
            }
        }
        if (synchronizationIndexes.isNotEmpty()) {
            if (SchemaConfig.modifyIndexes) {
                synchronizationIndexes.forEach { index ->
                    executeDropIndex(index)
                    executeCreateIndex(index)
                    logger.info("Successfully synchronized index '${index.indexName}' in table '$tableName'")
                }
            } else {
                logger.info("Table '$tableName' has ${synchronizationIndexes.size} synchronization indexes that are not synchronized in entity ${entityClass.simpleName}'")
            }
        }

        val missingIndexes = getMissing(existingIndexes, expectedIndexes) { it.indexName }
        val extraIndexes = getExtra(existingIndexes, expectedIndexes) { it.indexName }

        if (missingIndexes.isNotEmpty()) {
            if (SchemaConfig.createMissingIndexes) {
                missingIndexes.forEach { missingIndex ->
                    executeCreateIndex(missingIndex)
                    logger.info("Successfully created index '${missingIndex.indexName}' in table '$tableName'")
                }
            } else {
                logger.warn("Table '$tableName' has ${missingIndexes.size} missing indexes that are not defined in entity ${entityClass.simpleName}")
            }
        }
        if (extraIndexes.isNotEmpty()) {
            if (SchemaConfig.dropExistingIndexes) {
                extraIndexes.forEach { extraIndex ->
                    executeDropIndex(extraIndex)
                    logger.info("Successfully dropped index '${extraIndex.indexName}' in table '$tableName'")
                }
            } else {
                logger.warn("Table '$tableName' has ${extraIndexes.size} extra indexes that are not defined in entity ${entityClass.simpleName}")
            }
        }
        logger.info("Successfully synchronized indexes in table '$tableName'")
    }

    private fun executeDropIndex(index: IndexMeta) {
        val dropIndex = SqlNode.DropIndex(indexName = index.indexName, table = TableReference(index.tableName))
        ddlExecutor.executeDdl(dropIndex.getFirstSql(databaseValue.sqlDialect))
    }

    private fun executeCreateIndex(index: IndexMeta) {
        val createIndex = SqlNode.CreateIndex(
            indexName = index.indexName,
            table = TableReference(index.tableName),
            columns = index.columns,
            sorts = index.sorts,
            unique = index.unique,
            indexType = ""
        )
        ddlExecutor.executeDdl(createIndex.getFirstSql(databaseValue.sqlDialect))
    }

    /**
     * Add a single column to an existing table
     */
    private fun addColumnToTable(tableName: String, column: ColumnMeta, entityClass: KClass<*>) {
        logger.info("Adding column '${column.columnName}' to table '$tableName'")
        val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
        val alterTable = SqlNode.AlterTable(
            table = tableReference,
            operations = mutableListOf(AlterOperation.AddColumn(column))
        )
        ddlExecutor.executeDdlBatch(alterTable.getSqlList(databaseValue.sqlDialect))
        logger.info("Successfully added column '${column.columnName}' to table '$tableName'")
    }

    /**
     * Drop a single column from an existing table
     */
    private fun dropColumnFromTable(tableName: String, column: ColumnMeta, entityClass: KClass<*>) {
        logger.info("Dropping column '${column.columnName}' from table '$tableName'")
        val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
        val alterTable = SqlNode.AlterTable(
            table = tableReference,
            operations = mutableListOf(AlterOperation.DropColumn(column.columnName))
        )
        ddlExecutor.executeDdl(alterTable.getFirstSql(databaseValue.sqlDialect))
        logger.info("Successfully dropped column '${column.columnName}' from table '$tableName'")
    }

}
