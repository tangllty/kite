package com.tang.kite.schema

import com.tang.kite.annotation.Table
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.AlterOperation
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.ddl.SqlStatementDdlHandler
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

            val existingColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
            val expectedColumns = SchemaBuilder.getColumns(entityClass)
            synchronizeColumns(tableName, existingColumns, expectedColumns)
            val missingColumns = expectedColumns.filter { expectedColumn ->
                !existingColumns.any { existingColumn ->
                    existingColumn.columnName.equals(expectedColumn.columnName, ignoreCase = true)
                }
            }
            val extraColumns = existingColumns.filter { existingColumn ->
                !expectedColumns.any { expectedColumn ->
                    existingColumn.columnName.equals(expectedColumn.columnName, ignoreCase = true)
                }
            }

            if (missingColumns.isEmpty() && extraColumns.isEmpty()) {
                logger.info("Table '$tableName' is already synchronized, no changes needed")
                return
            }
            if (missingColumns.isNotEmpty()) {
                if (!SchemaConfig.createMissingColumns) {
                    logger.warn("Table '$tableName' has ${missingColumns.size} missing columns that are not defined in entity ${entityClass.simpleName}")
                } else {
                    logger.info("Found ${missingColumns.size} missing columns in table '$tableName'")
                    missingColumns.forEach { missingColumn ->
                        addColumnToTable(tableName, missingColumn, entityClass)
                    }
                }
            }
            if (extraColumns.isNotEmpty()) {
                if (!SchemaConfig.dropExistingColumns) {
                    logger.warn("Table '$tableName' has ${extraColumns.size} extra columns that are not defined in entity ${entityClass.simpleName}")
                } else {
                    logger.info("Found ${extraColumns.size} extra columns in table '$tableName'")
                    extraColumns.forEach { extraColumn ->
                        dropColumnFromTable(tableName, extraColumn, entityClass)
                    }
                }
            }
            logger.info("Successfully synchronized table '$tableName'")
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
        val success = ddlExecutor.executeDdlBatch(createTableNode.getSqlList(databaseValue.sqlDialect))
        if (success) {
            logger.info("Successfully created table '$tableName'")
        } else {
            logger.error("Failed to create table '$tableName'")
        }
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

        val success = ddlExecutor.executeDdl(sql)
        if (success) {
            logger.info("Successfully updated table comment for '$tableName'")
        } else {
            logger.error("Failed to update table comment for '$tableName'")
        }
    }

    private fun synchronizeColumns(tableName: String, existingColumns: List<ColumnMeta>, expectedColumns: List<ColumnMeta>) {
        existingColumns.forEach { existingColumn ->
            val expectedColumn = expectedColumns.firstOrNull { existingColumn.columnName.equals(it.columnName, ignoreCase = true) }
            if (expectedColumn != null) {
                synchronizeColumn(tableName, existingColumn, expectedColumn)
            }
        }
    }

    private fun synchronizeColumn(tableName: String, existingColumn: ColumnMeta, expectedColumn: ColumnMeta) {
        if (existingColumn == expectedColumn) {
            return
        }
        logger.info("Updating column '${existingColumn.columnName}' in table '$tableName'")
        if (existingColumn.comment.equals(expectedColumn.comment, ignoreCase = true).not()) {
            val dialect = databaseValue.sqlDialect
            val sql = SqlStatementDdlHandler.getColumnComment(tableName, expectedColumn, dialect)
            val success = ddlExecutor.executeDdl(sql)
            if (success) {
                logger.info("Successfully updated column comment for '${existingColumn.columnName}' in table '$tableName'")
            } else {
                logger.error("Failed to update column comment for '${existingColumn.columnName}' in table '$tableName'")
            }
        }
        if (existingColumn.typeName.equals(expectedColumn.typeName, ignoreCase = true).not()) {
            println("Column '${existingColumn.columnName}' type changed from '${existingColumn.typeName}' to '${expectedColumn.typeName}'")
        }
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
        val success = ddlExecutor.executeDdlBatch(alterTable.getSqlList(databaseValue.sqlDialect))
        if (success) {
            logger.info("Successfully added column '${column.columnName}' to table '$tableName'")
        } else {
            logger.error("Failed to add column '${column.columnName}' to table '$tableName'")
        }
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
        val success = ddlExecutor.executeDdl(alterTable.getFirstSql(databaseValue.sqlDialect))
        if (success) {
            logger.info("Successfully dropped column '${column.columnName}' from table '$tableName'")
        } else {
            logger.error("Failed to drop column '${column.columnName}' from table '$tableName'")
        }
    }

}
