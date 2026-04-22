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
        if (properties.driver == null) {
            throw IllegalArgumentException("driver is required")
        }
        if (properties.url == null) {
            throw IllegalArgumentException("url is required")
        }
        return UnpooledDataSource(properties)
    }

}
