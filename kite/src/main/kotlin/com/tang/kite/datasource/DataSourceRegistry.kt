package com.tang.kite.datasource

import com.tang.kite.logging.getLogger
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.factory.defaults.DefaultSqlDialectFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.sql.DataSource

/**
 * @author Tang
 */
object DataSourceRegistry {

    private val logger = getLogger

    private val databaseMap: ConcurrentMap<String, DatabaseValue> = ConcurrentHashMap()

    /**
     * Whether to override the existing data source when registering a new one.
     */
    @JvmField
    var override: Boolean = false

    @JvmStatic
    fun registerBatch(databaseMap: Map<String, DatabaseValue>) {
        require(databaseMap.isNotEmpty()) { "DataSource map cannot be empty" }
        databaseMap.forEach { checkKey(it.key, it.value) }
        DataSourceRegistry.databaseMap.putAll(databaseMap)
        logger.info("Batch registered data sources, count: [{}]", databaseMap.size)
    }

    @JvmStatic
    fun register(dataSourceKey: String, database: DatabaseValue) {
        checkKey(dataSourceKey, database)
        databaseMap[dataSourceKey] = database
        logger.info("Registered data source: [$dataSourceKey], database type: [${database.databaseType}]")
    }

    @JvmStatic
    fun register(dataSourceKey: String, dataSource: DataSource, databaseType: DatabaseType) {
        checkKey(dataSourceKey)
        val sqlDialect = DefaultSqlDialectFactory.createSqlDialect(databaseType)
        register(dataSourceKey, DatabaseValue(dataSource, databaseType, sqlDialect))
    }

    @JvmStatic
    fun register(dataSourceKey: String, dataSource: DataSource) {
        checkKey(dataSourceKey)
        val databaseType = getDatabaseType(dataSource)
        register(dataSourceKey, dataSource, databaseType)
    }

    @JvmStatic
    fun get(dataSourceKey: String): DatabaseValue {
        checkKey(dataSourceKey)
        return databaseMap[dataSourceKey] ?: throw IllegalArgumentException("DataSource key [$dataSourceKey] does not exist in the registry")
    }

    @JvmStatic
    fun getKeys(): List<String> {
        return databaseMap.keys.toList()
    }

    @JvmStatic
    fun unregister(dataSourceKey: String) {
        checkKey(dataSourceKey)
        checkExists(dataSourceKey)
        databaseMap.remove(dataSourceKey)
        logger.info("Unregistered data source: [{}]", dataSourceKey)
    }

    @JvmStatic
    fun unregisterBatch(dataSourceKeys: List<String>) {
        require(dataSourceKeys.isNotEmpty()) { "DataSource key list cannot be empty" }
        dataSourceKeys.forEach { checkExists(it) }
        dataSourceKeys.forEach { unregister(it) }
        logger.info("Batch unregistered data sources, count: [{}]", dataSourceKeys.size)
    }

    @JvmStatic
    fun clear() {
        val size = databaseMap.size
        databaseMap.clear()
        logger.info("Cleared all data sources, count: [{}]", size)
    }

    private fun getDatabaseType(dataSource: DataSource): DatabaseType {
        return dataSource.connection.use {
            val url = it.metaData.url
            DatabaseType.getDatabaseType(url)
        }
    }

    private fun checkKey(key: String) {
        require(key.isNotBlank()) { "DataSource key cannot be empty or blank" }
    }

    private fun checkKey(key: String, database: DatabaseValue) {
        checkKey(key)
        if (databaseMap.containsKey(key).not()) {
            return
        }
        if (override) {
            logger.info("Overridden data source: [$key], database type: [${database.databaseType}]")
        } else {
            throw IllegalArgumentException("DataSource key already exists ($key)")
        }
    }

    private fun checkExists(key: String) {
        require(databaseMap.containsKey(key)) { "DataSource key [$key] does not exist in the registry" }
    }

}
