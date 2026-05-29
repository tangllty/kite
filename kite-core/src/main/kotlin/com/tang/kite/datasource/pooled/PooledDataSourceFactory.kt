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
        requireNotNull(properties.driver) { "driver is required" }
        requireNotNull(properties.url) { "url is required" }
        return PooledDataSource(properties)
    }

}
