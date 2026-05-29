package com.tang.kite.datasource.unpooled

import com.tang.kite.datasource.DataSourceFactory
import javax.sql.DataSource

/**
 * @author Tang
 */
class UnpooledDataSourceFactory(private val properties: UnpooledProperties) : DataSourceFactory<UnpooledProperties> {

    override fun getProperties(): UnpooledProperties {
        return properties
    }

    override fun getDataSource(): DataSource {
        requireNotNull(properties.driver) { "driver is required" }
        requireNotNull(properties.url) { "url is required" }
        return UnpooledDataSource(properties)
    }

}
