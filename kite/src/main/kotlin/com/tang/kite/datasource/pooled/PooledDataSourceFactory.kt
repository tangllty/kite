package com.tang.kite.datasource.pooled

import com.tang.kite.datasource.DataSourceFactory
import com.tang.kite.io.Resources
import javax.sql.DataSource

/**
 * @author Tang
 */
class PooledDataSourceFactory(private val properties: Map<String, String>) : DataSourceFactory {

    override fun getProperties(): Map<String, String> {
        return properties
    }

    override fun getDataSource(): DataSource {
        val pooledProperties = Resources.propertyToObject(properties, PooledProperties::class.java)
        if (pooledProperties.driver == null) {
            throw IllegalArgumentException("driver is required")
        }
        if (pooledProperties.url == null) {
            throw IllegalArgumentException("url is required")
        }
        return PooledDataSource(pooledProperties)
    }

}
