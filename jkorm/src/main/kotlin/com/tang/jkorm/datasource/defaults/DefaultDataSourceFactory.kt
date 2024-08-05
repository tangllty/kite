package com.tang.jkorm.datasource.defaults

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource
import com.tang.jkorm.datasource.DataSourceFactory
import javax.sql.DataSource

/**
 * @author Tang
 */
class DefaultDataSourceFactory(private val properties: Map<String, String>) : DataSourceFactory {

    override fun getProperties(): Map<String, String> {
        return properties
    }

    override fun getDataSource(): DataSource {
        val dataSource = MysqlConnectionPoolDataSource()
        dataSource.setURL(properties["url"])
        dataSource.user = properties["username"]
        dataSource.password = properties["password"]
        return dataSource
    }

}
