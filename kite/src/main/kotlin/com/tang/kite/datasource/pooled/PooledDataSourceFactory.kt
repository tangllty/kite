package com.tang.kite.datasource.pooled

import com.tang.kite.datasource.DataSourceFactory
import javax.sql.DataSource

/**
 * @author Tang
 */
class PooledDataSourceFactory(private val properties: PooledProperties) : DataSourceFactory<PooledProperties> {

    override fun getProperties(): PooledProperties {
        return properties
    }

    override fun getDataSource(): DataSource {
        if (properties.driver == null) {
            throw IllegalArgumentException("driver is required")
        }
        if (properties.url == null) {
            throw IllegalArgumentException("url is required")
        }
        return PooledDataSource(properties)
    }

}
