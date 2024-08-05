package com.tang.jkorm.transaction

import com.tang.jkorm.session.TransactionIsolationLevel
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
interface TransactionFactory {

    fun newTransaction(connection: Connection): Transaction

    fun newTransaction(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): Transaction

}
