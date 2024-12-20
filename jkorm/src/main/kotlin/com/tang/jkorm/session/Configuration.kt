package com.tang.jkorm.session

import com.tang.jkorm.sql.provider.SqlProvider
import com.tang.jkorm.transaction.TransactionFactory
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
