package com.tang.kite.transaction.jdbc

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import com.tang.kite.transaction.Transaction
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
class JdbcTransaction : Transaction {

    private val logger = LoggerFactory.getLogger(JdbcTransaction::class.java)

    private lateinit var dataSource: DataSource
    private lateinit var connection: Connection
    private var isolationLevel: TransactionIsolationLevel? = null
    private var autoCommit: Boolean = false

    constructor(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean) {
        this.dataSource = dataSource
        this.isolationLevel = isolationLevel
        this.autoCommit = autoCommit
    }

    constructor(connection: Connection) {
        this.connection = connection
    }

    override fun getConnection(): Connection {
        if (::connection.isInitialized.not() || connection.isClosed) {
            openConnection()
        }
        return connection
    }

    private fun openConnection() {
        val start = System.currentTimeMillis()
        connection = dataSource.connection
        if (logger.isDebugEnabled) {
            logger.debug("Opening JDBC Connection [{}] in {} ms", connection, System.currentTimeMillis() - start)
        }
        isolationLevel?.let {
            connection.transactionIsolation = it.level
        }
        logger.debug("Setting autocommit to [{}] on JDBC Connection [{}]", autoCommit, connection)
        this.connection.autoCommit = autoCommit
    }

    override fun commit() {
        logger.debug("Committing JDBC Connection [{}]", connection)
        connection.commit()
    }

    override fun rollback() {
        logger.debug("Rolling back JDBC Connection [{}]", connection)
        connection.rollback()
    }

    override fun close() {
        if (connection.autoCommit.not()) {
            logger.debug("Resetting autocommit to true on JDBC Connection [{}]", connection)
            connection.autoCommit = true
        }
        logger.debug("Closing JDBC Connection [{}]", connection)
        connection.close()
    }

}
