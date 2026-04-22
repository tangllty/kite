package com.tang.kite.transaction.jdbc

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import com.tang.kite.transaction.Transaction
import com.tang.kite.transaction.TransactionFactory
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
class JdbcTransactionFactory : TransactionFactory {

    override fun newTransaction(connection: Connection): Transaction {
        return JdbcTransaction(connection)
    }

    override fun newTransaction(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): Transaction {
        return JdbcTransaction(dataSource, isolationLevel, autoCommit)
    }

}
