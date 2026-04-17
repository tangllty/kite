package com.tang.kite.datasource

import java.io.PrintWriter
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * @author Tang
 */
abstract class AbstractDataSource : DataSource {

    override fun getLogWriter(): PrintWriter? {
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

    override fun getParentLogger(): Logger? {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
    }

    override fun <T> unwrap(iface: Class<T?>?): T? {
        throw SQLException(javaClass.name + " is not a wrapper.")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface?.isInstance(this) ?: false
    }

}
