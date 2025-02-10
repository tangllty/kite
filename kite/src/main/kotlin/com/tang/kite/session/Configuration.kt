package com.tang.kite.session

import com.tang.kite.sql.provider.SqlProvider
import com.tang.kite.transaction.TransactionFactory
import javax.sql.DataSource

/**
 * Configuration
 *
 * @author Tang
 */
class Configuration(

    val dataSource: DataSource,

    val sqlProvider: SqlProvider,

    val transactionFactory: TransactionFactory

)
