package com.tang.kite.datasource.unpooled

import com.tang.kite.datasource.DataSourceFactory
import javax.sql.DataSource

/**
 * @author Tang
 */
class UnpooledDataSourceFactory(private val properties: Map<String, String>) : DataSourceFactory {

    override fun getProperties(): Map<String, String> {
        return properties
    }

    override fun getDataSource(): DataSource {
        val driver = properties["driver"] ?: throw IllegalArgumentException("driver is required")
        val url = properties["url"] ?: throw IllegalArgumentException("url is required")
        val username = properties["username"]
        val password = properties["password"]
        val unpooledProperties = UnpooledProperties(driver, url, username, password)
        return UnpooledDataSource(unpooledProperties)
    }

}
