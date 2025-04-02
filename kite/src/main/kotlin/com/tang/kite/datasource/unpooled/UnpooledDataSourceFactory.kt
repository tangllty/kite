package com.tang.kite.datasource.unpooled

import com.tang.kite.datasource.DataSourceFactory
import com.tang.kite.io.Resources
import javax.sql.DataSource

/**
 * @author Tang
 */
class UnpooledDataSourceFactory(private val properties: Map<String, String>) : DataSourceFactory {

    override fun getProperties(): Map<String, String> {
        return properties
    }

    override fun getDataSource(): DataSource {
        val unpooledProperties = Resources.propertyToObject(properties, UnpooledProperties::class.java)
        if (unpooledProperties.driver == null) {
            throw IllegalArgumentException("driver is required")
        }
        if (unpooledProperties.url == null) {
            throw IllegalArgumentException("url is required")
        }
        return UnpooledDataSource(unpooledProperties)
    }

}
