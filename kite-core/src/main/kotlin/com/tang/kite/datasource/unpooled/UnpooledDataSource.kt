package com.tang.kite.datasource.unpooled

import com.tang.kite.datasource.AbstractDataSource
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.Executors

/**
 * @author Tang
 */
class UnpooledDataSource(private val properties: UnpooledProperties) : AbstractDataSource() {

    init {
        Class.forName(properties.driver)
    }

    override fun getConnection(): Connection {
        return getConnection(properties.username, properties.password)
    }

    override fun getConnection(username: String?, password: String?): Connection {
        val connection = DriverManager.getConnection(properties.url, username, password)
        configureConnection(connection)
        return connection
    }

    private fun configureConnection(connection: Connection) {
        if (properties.transactionIsolation != null) {
            connection.transactionIsolation = properties.transactionIsolation!!.level
        }
        if (properties.networkTimeout != null) {
            connection.setNetworkTimeout(Executors.newSingleThreadExecutor(), properties.networkTimeout!!)
        }
    }

}
