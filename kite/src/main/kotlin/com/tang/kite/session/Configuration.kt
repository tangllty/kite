package com.tang.kite.session

import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.transaction.TransactionFactory
import javax.sql.DataSource

/**
 * Configuration
 *
 * @author Tang
 */
class Configuration(

    val dataSource: DataSource,

    val sqlDialect: SqlDialect,

    val transactionFactory: TransactionFactory

)
