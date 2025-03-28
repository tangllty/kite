package com.tang.kite.spring.transaction

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import com.tang.kite.transaction.Transaction
import com.tang.kite.transaction.TransactionFactory
import java.sql.Connection
import javax.sql.DataSource

/**
 * Spring transaction factory
 *
 * @author Tang
 */
class SpringTransactionFactory : TransactionFactory {

    override fun newTransaction(connection: Connection): Transaction {
        return SpringTransaction(connection)
    }

    override fun newTransaction(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): Transaction {
        return SpringTransaction(dataSource, isolationLevel, autoCommit)
    }

}
