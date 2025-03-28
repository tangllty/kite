package com.tang.kite.datasource.unpooled

import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.concurrent.Executors
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * @author Tang
 */
class UnpooledDataSource(private val properties: UnpooledProperties) : DataSource {

    init {
        Class.forName(properties.driver)
    }

    override fun getLogWriter(): PrintWriter {
        return DriverManager.getLogWriter()
    }

    override fun setLogWriter(out: PrintWriter?) {
        DriverManager.setLogWriter(out)
    }

    override fun setLoginTimeout(seconds: Int) {
        DriverManager.setLoginTimeout(seconds)
    }

    override fun getLoginTimeout(): Int {
        return DriverManager.getLoginTimeout()
    }

    override fun getParentLogger(): Logger {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        throw SQLException(javaClass.name + " is not a wrapper.")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return false
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
