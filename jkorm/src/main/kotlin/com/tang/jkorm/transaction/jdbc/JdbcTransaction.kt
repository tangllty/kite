package com.tang.jkorm.transaction.jdbc

import com.tang.jkorm.session.TransactionIsolationLevel
import com.tang.jkorm.transaction.Transaction
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
class JdbcTransaction : Transaction {

    private val connection: Connection
    private lateinit var dataSource: DataSource
    private var isolationLevel: TransactionIsolationLevel? = null
    private var autoCommit: Boolean = false

    constructor(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean) {
        this.connection = dataSource.connection
        this.dataSource = dataSource
        this.isolationLevel = isolationLevel
        this.autoCommit = autoCommit
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
        connection.commit()
    }

    override fun rollback() {
        connection.rollback()
    }

    override fun close() {
        connection.close()
    }

}
