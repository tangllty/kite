package com.tang.kite.datasource.defaults

import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * @author Tang
 */
class DefaultDataSource(

    private var driver: String,

    private var url: String,

    private var username: String?,

    private var password: String?

) : DataSource {

    init {
        Class.forName(driver)
    }

    override fun getLogWriter(): PrintWriter {
        TODO("Not yet implemented")
    }

    override fun setLogWriter(out: PrintWriter?) {
        TODO("Not yet implemented")
    }

    override fun setLoginTimeout(seconds: Int) {
        TODO("Not yet implemented")
    }

    override fun getLoginTimeout(): Int {
        TODO("Not yet implemented")
    }

    override fun getParentLogger(): Logger {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConnection(): Connection {
        return DriverManager.getConnection(url, username, password)
    }

    override fun getConnection(username: String?, password: String?): Connection {
        return DriverManager.getConnection(url, username, password)
    }

}
