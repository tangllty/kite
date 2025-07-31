package com.tang.kite.datasource.pooled

import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.lang.UnsupportedOperationException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * @author Tang
 */
class PooledDataSource(private val properties: PooledProperties) : DataSource {

    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(PooledDataSource::class.java)

    private val freeConnections: BlockingQueue<Connection> = LinkedBlockingQueue(properties.maximumFreeConnections)

    private val activeConnections: BlockingQueue<Connection> = LinkedBlockingQueue(properties.maximumActiveConnections)

    init {
        val start = System.currentTimeMillis()
        Class.forName(properties.driver)
        repeat(properties.initialConnections) {
            freeConnections.add(newConnection())
        }
        logger.debug("Initialized pooled data source with ${properties.initialConnections} connections in ${System.currentTimeMillis() - start} ms.")
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
        if (freeConnections.isEmpty() || freeConnections.size < properties.minimumFreeConnections) {
            if (activeConnections.size < properties.maximumActiveConnections) {
                val connection = newConnection()
                activeConnections.add(connection)
                logger.debug("Pooled connection [{}] is created and added to the pool.", connection)
                return connection
            } else {
                throw SQLException("Maximum active connections reached: ${properties.maximumActiveConnections}")
            }
        }
        val connection = freeConnections.take()
        activeConnections.add(connection)
        logger.debug("Pooled connection [{}] is taken from the pool.", connection)
        return connection
    }

    override fun getConnection(username: String?, password: String?): Connection {
        throw UnsupportedOperationException("Not supported yet.")
    }

    private fun configureConnection(connection: Connection) {
        if (properties.transactionIsolation != null) {
            connection.transactionIsolation = properties.transactionIsolation!!.level
        }
        if (properties.networkTimeout != null) {
            connection.setNetworkTimeout(Executors.newSingleThreadExecutor(), properties.networkTimeout!!)
        }
    }

    @Synchronized
    private fun newConnection(): Connection {
        val connection = DriverManager.getConnection(properties.url, properties.username, properties.password)
        configureConnection(connection)
        return PooledConnection(connection, this)
    }

    /**
     * Releases the connection back to the pool. If the pool is full, the connection is closed.
     *
     * @param connection the connection to be released.
     * @returns true if the connection is released back to the pool, false if the connection is closed.
     */
    @Synchronized
    fun releaseConnection(connection: Connection): Boolean {
        activeConnections.remove(connection)
        if (freeConnections.size >= properties.maximumFreeConnections) {
            logger.debug("Pooled connection [{}] is closed and removed from the pool.", connection)
            return false
        }
        freeConnections.add(connection)
        logger.debug("Pooled connection [{}] is released back to the pool.", connection)
        return true
    }

}
