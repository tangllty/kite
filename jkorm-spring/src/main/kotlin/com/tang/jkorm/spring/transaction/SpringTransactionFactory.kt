package com.tang.jkorm.spring.transaction

import com.tang.jkorm.session.TransactionIsolationLevel
import com.tang.jkorm.transaction.Transaction
import com.tang.jkorm.transaction.TransactionFactory
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
