package com.tang.kite.schema

import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.AlterOperation
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.ddl.SqlStatementDdlHandler
import com.tang.kite.sql.datatype.DataType
import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class ColumnSynchronization(

    private val databaseValue: DatabaseValue,

    private val ddlExecutor: DdlExecutor

) {

    private val logger = getLogger

    fun synchronizeColumns(entityClass: KClass<*>, tableName: String) {
        val existingColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        val expectedColumns = SchemaBuilder.buildColumns(entityClass)
        existingColumns.forEach { existingColumn ->
            val expectedColumn = expectedColumns.firstOrNull { existingColumn.columnName.equals(it.columnName, ignoreCase = true) }
            if (expectedColumn != null) {
                synchronizeColumn(tableName, existingColumn, expectedColumn)
            }
        }
        val missingColumns = SchemaSynchronization.getMissing(existingColumns, expectedColumns) { it.columnName }
        val extraColumns = SchemaSynchronization.getExtra(existingColumns, expectedColumns) { it.columnName }

        if (missingColumns.isEmpty() && extraColumns.isEmpty()) {
            logger.info("Columns in table '$tableName' are already synchronized, no changes needed")
        }
        handleMissingColumns(tableName, missingColumns, entityClass)
        handleExtraColumns(tableName, extraColumns, entityClass)
        logger.info("Successfully synchronized columns in table '$tableName'")
    }

    private fun synchronizeColumn(tableName: String, existingColumn: ColumnMeta, expectedColumn: ColumnMeta) {
        if (existingColumn == expectedColumn) {
            return
        }
        if (existingColumn.comment != expectedColumn.comment) {
            logger.info("Updating column comment for '${existingColumn.columnName}' from '${existingColumn.comment}' to '$expectedColumn.comment'")
            val dialect = databaseValue.sqlDialect
            val sql = SqlStatementDdlHandler.getColumnComment(tableName, expectedColumn, dialect)
            ddlExecutor.executeDdl(sql)
        }
        val expectedColumnTypeName = DataType.normalize(expectedColumn.typeName)
        val existingColumnTypeName = DataType.normalize(existingColumn.typeName)
        if (existingColumnTypeName != expectedColumnTypeName) {
            logger.info("Updating column type for '${existingColumn.columnName}' from '${existingColumn.typeName}' to '$expectedColumn.typeName'")
            val alterTable = SqlNode.AlterTable(
                table = TableReference(tableName),
                operations = mutableListOf(AlterOperation.ModifyColumn(expectedColumn))
            )
            ddlExecutor.executeDdlBatch(alterTable.getSqlList(databaseValue.sqlDialect))
        }
    }

    private fun handleMissingColumns(tableName: String, missingColumns: List<ColumnMeta>, entityClass: KClass<*>) {
        if (missingColumns.isEmpty()) return
        if (SchemaConfig.createMissingColumns) {
            logger.info("Found ${missingColumns.size} missing columns in table '$tableName'")
            missingColumns.forEach { addColumn(tableName, it, entityClass) }
        } else {
            logger.warn("Table '$tableName' has ${missingColumns.size} missing columns that are not defined in entity ${entityClass.simpleName}")
        }
    }

    private fun handleExtraColumns(tableName: String, extraColumns: List<ColumnMeta>, entityClass: KClass<*>) {
        if (extraColumns.isEmpty()) return
        if (SchemaConfig.dropExistingColumns) {
            logger.info("Found ${extraColumns.size} extra columns in table '$tableName'")
            extraColumns.forEach { dropColumn(tableName, it, entityClass) }
        } else {
            logger.warn("Table '$tableName' has ${extraColumns.size} extra columns that are not defined in entity ${entityClass.simpleName}")
        }
    }

    /**
     * Add a single column to an existing table
     */
    private fun addColumn(tableName: String, column: ColumnMeta, entityClass: KClass<*>) {
        logger.info("Adding column '${column.columnName}' to table '$tableName'")
        val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
        val alterTable = SqlNode.AlterTable(
            table = tableReference,
            operations = mutableListOf(AlterOperation.AddColumn(column))
        )
        ddlExecutor.executeDdlBatch(alterTable.getSqlList(databaseValue.sqlDialect))
    }

    /**
     * Drop a single column from an existing table
     */
    private fun dropColumn(tableName: String, column: ColumnMeta, entityClass: KClass<*>) {
        logger.info("Dropping column '${column.columnName}' from table '$tableName'")
        val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
        val alterTable = SqlNode.AlterTable(
            table = tableReference,
            operations = mutableListOf(AlterOperation.DropColumn(column.columnName))
        )
        ddlExecutor.executeDdl(alterTable.getFirstSql(databaseValue.sqlDialect))
    }

}
