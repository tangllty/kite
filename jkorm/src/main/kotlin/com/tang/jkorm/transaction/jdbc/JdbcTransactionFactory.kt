package com.tang.jkorm.transaction.jdbc

import com.tang.jkorm.session.TransactionIsolationLevel
import com.tang.jkorm.transaction.Transaction
import com.tang.jkorm.transaction.TransactionFactory
import java.sql.Connection
import javax.sql.DataSource

class JdbcTransactionFactory : TransactionFactory {

    override fun newTransaction(connection: Connection): Transaction {
        return JdbcTransaction(connection)
    }

    override fun newTransaction(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): Transaction {
        return JdbcTransaction(dataSource, isolationLevel, autoCommit)
    }

}
