package com.tang.kite.transaction

import com.tang.kite.session.TransactionIsolationLevel
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
interface TransactionFactory {

    fun newTransaction(connection: Connection): Transaction

    fun newTransaction(dataSource: DataSource, isolationLevel: TransactionIsolationLevel?, autoCommit: Boolean): Transaction

}
