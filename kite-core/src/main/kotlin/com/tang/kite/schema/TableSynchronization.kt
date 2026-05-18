package com.tang.kite.schema

import com.tang.kite.annotation.Table
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * @author Tang
 */
class TableSynchronization(private val databaseValue: DatabaseValue) {

    private val logger = getLogger

    private val ddlExecutor = DdlExecutor(databaseValue)

    private val columnSynchronization = ColumnSynchronization(databaseValue, ddlExecutor)

    private val indexSynchronization = IndexSynchronization(databaseValue, ddlExecutor)

    /**
     * Synchronize table structure by adding missing columns
     */
    fun synchronizeTables(entityClasses: Set<KClass<*>>) {
        entityClasses.forEach { entityClass ->
            val tableName = Reflects.getTableName(entityClass.java)
            val tableExists = MetaDataHandlers.tableExists(databaseValue, tableName)
            if (SchemaConfig.dropExistingTables) {
                dropTable(entityClass, tableName, tableExists)
            }
            if (shouldCreateTable(tableExists)) {
                createTable(entityClass, tableName, tableExists)
                return@forEach
            }
            logger.info("Synchronizing table '$tableName' for entity ${entityClass.simpleName}")
            synchronizeTable(entityClass, tableName)
            columnSynchronization.synchronizeColumns(entityClass, tableName)
            indexSynchronization.synchronizeIndexes(entityClass, tableName)
            logger.info("Successfully synchronized table '$tableName'")
        }
    }

    private fun shouldCreateTable(tableExists: Boolean): Boolean {
        return (SchemaConfig.createMissingTables && !tableExists) || SchemaConfig.dropExistingTables
    }

    /**
     * Drop table for entity if exists
     */
    private fun dropTable(entityClass: KClass<*>, tableName: String, tableExists: Boolean) {
        if (tableExists) {
            logger.info("Dropping table '$tableName' for entity ${entityClass.simpleName}")
            val tableReference = TableReference(tableName, entityClass.java, Reflects.getTableAlias(entityClass.java))
            val dropTable = SqlNode.DropTable(table = tableReference)
            ddlExecutor.executeDdl(dropTable.getFirstSql(databaseValue.sqlDialect))
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
    }

    private fun synchronizeTable(entityClass: KClass<*>, tableName: String) {
        val tableAnnotation = entityClass.findAnnotation<Table>()
        val expectedComment = tableAnnotation?.comment
        val existingTable = MetaDataHandlers.getTable(databaseValue, tableName)
        val existingComment = existingTable?.comment
        if (existingComment == expectedComment) {
            return
        }
        logger.info("Updating table comment for '$tableName' from '${existingComment}' to '$expectedComment'")
        val sql = buildTableCommentSql(tableName, expectedComment)
        ddlExecutor.executeDdl(sql)
    }

    private fun buildTableCommentSql(tableName: String, comment: String?): String {
        val dialect = databaseValue.sqlDialect
        return if (dialect.supportsCommentOnTable()) {
            if (comment == null) {
                "drop comment on table $tableName"
            } else {
                "comment on table $tableName is '$comment'"
            }
        } else {
            "alter table $tableName comment '${comment ?: ""}'"
        }
    }

}
