package com.tang.kite.datasource.pooled

import com.tang.kite.datasource.DataSourceFactory
import javax.sql.DataSource

/**
 * @author Tang
 */
class PooledDataSourceFactory(private val properties: Map<String, String>) : DataSourceFactory {

    override fun getProperties(): Map<String, String> {
        return properties
    }

    override fun getDataSource(): DataSource {
        val driver = properties["driver"] ?: throw IllegalArgumentException("driver is required")
        val url = properties["url"] ?: throw IllegalArgumentException("url is required")
        val username = properties["username"]
        val password = properties["password"]
        val pooledProperties = PooledProperties(driver, url, username, password)
        return PooledDataSource(pooledProperties)
    }

}
