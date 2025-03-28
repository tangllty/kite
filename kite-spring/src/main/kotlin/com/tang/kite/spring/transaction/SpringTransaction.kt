package com.tang.kite.spring.transaction

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import com.tang.kite.transaction.Transaction
import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.Connection
import javax.sql.DataSource

/**
 * Spring transaction
 *
 * @author Tang
 */
class SpringTransaction : Transaction {

    private val connection: Connection
    private lateinit var dataSource: DataSource
    private var isolationLevel: TransactionIsolationLevel? = null
    private var autoCommit: Boolean = false
    private var isConnectionTransactional: Boolean = false

    constructor(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean) {
        this.connection = DataSourceUtils.getConnection(dataSource)
        this.dataSource = dataSource
        this.isolationLevel = isolationLevel
        this.autoCommit = autoCommit
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection, dataSource)
    }

    constructor(connection: Connection) {
        this.connection = connection
    }

    override fun getConnection(): Connection {
        isolationLevel?.let {
            connection.transactionIsolation = it.level
        }
        this.connection.autoCommit = autoCommit
        return connection
    }

    override fun commit() {
        if (!isConnectionTransactional && !autoCommit) {
            connection.commit()
        }
    }

    override fun rollback() {
        if (!isConnectionTransactional && !autoCommit) {
            connection.rollback()
        }
    }

    override fun close() {
        DataSourceUtils.releaseConnection(connection, dataSource)
    }

}
