package com.tang.kite.schema

import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger
import com.tang.kite.metadata.IndexMeta
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.SqlNode
import kotlin.collections.forEach
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class IndexSynchronization(

    private val databaseValue: DatabaseValue,

    private val ddlExecutor: DdlExecutor

) {

    private val logger = getLogger

    fun synchronizeIndexes(entityClass: KClass<*>, tableName: String) {
        val existingIndexes = MetaDataHandlers.getIndexes(databaseValue, tableName)
            .map { it.value }
            .filter { it.primaryKey.not() }
        val expectedIndexes = SchemaBuilder.buildIndexes(entityClass)

        val missingIndexes = SchemaSynchronization.getMissing(existingIndexes, expectedIndexes) { it.indexName }
        val extraIndexes = SchemaSynchronization.getExtra(existingIndexes, expectedIndexes) { it.indexName }
        val modifiedIndexes = findModifiedIndexes(existingIndexes, expectedIndexes)
        if (missingIndexes.isEmpty() && extraIndexes.isEmpty() && modifiedIndexes.isEmpty()) {
            logger.info("Indexes in table '$tableName' are already synchronized, no changes needed")
        }
        handleModifiedIndexes(tableName, modifiedIndexes, entityClass)
        handleMissingIndexes(tableName, missingIndexes, entityClass)
        handleExtraIndexes(tableName, extraIndexes, entityClass)
        logger.info("Successfully synchronized indexes in table '$tableName'")
    }

    private fun findModifiedIndexes(existingIndexes: List<IndexMeta>, expectedIndexes: List<IndexMeta>): List<IndexMeta> {
        return expectedIndexes.filter { expectedIndex ->
            val existingIndex = existingIndexes.firstOrNull { expectedIndex.indexName.equals(it.indexName, ignoreCase = true) }
            existingIndex != null && (existingIndex.columns != expectedIndex.columns
                || existingIndex.sorts != expectedIndex.sorts
                || existingIndex.unique != expectedIndex.unique)
        }
    }

    private fun handleModifiedIndexes(tableName: String, modifiedIndexes: List<IndexMeta>, entityClass: KClass<*>) {
        if (modifiedIndexes.isEmpty()) return
        if (SchemaConfig.modifyIndexes) {
            logger.info("Found ${modifiedIndexes.size} modified indexes in table '$tableName'")
            modifiedIndexes.forEach { index ->
                dropIndex(index)
                createIndex(index)
            }
        } else {
            logger.info("Table '$tableName' has ${modifiedIndexes.size} modified indexes that are not synchronized in entity ${entityClass.simpleName}")
        }
    }

    private fun handleMissingIndexes(tableName: String, missingIndexes: List<IndexMeta>, entityClass: KClass<*>) {
        if (missingIndexes.isEmpty()) return
        if (SchemaConfig.createMissingIndexes) {
            logger.info("Found ${missingIndexes.size} missing indexes in table '$tableName'")
            missingIndexes.forEach { createIndex(it) }
        } else {
            logger.warn("Table '$tableName' has ${missingIndexes.size} missing indexes that are not defined in entity ${entityClass.simpleName}")
        }
    }

    private fun handleExtraIndexes(tableName: String, extraIndexes: List<IndexMeta>, entityClass: KClass<*>) {
        if (extraIndexes.isEmpty()) return
        if (SchemaConfig.dropExistingIndexes) {
            logger.info("Found ${extraIndexes.size} extra indexes in table '$tableName'")
            extraIndexes.forEach { dropIndex(it) }
        } else {
            logger.warn("Table '$tableName' has ${extraIndexes.size} extra indexes that are not defined in entity ${entityClass.simpleName}")
        }
    }

    private fun createIndex(index: IndexMeta) {
        logger.info("Creating index '${index.indexName}' in table '${index.tableName}'")
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

    private fun dropIndex(index: IndexMeta) {
        logger.info("Dropping index '${index.indexName}' in table '${index.tableName}'")
        val dropIndex = SqlNode.DropIndex(indexName = index.indexName, table = TableReference(index.tableName))
        ddlExecutor.executeDdl(dropIndex.getFirstSql(databaseValue.sqlDialect))
    }

}
