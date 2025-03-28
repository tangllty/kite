package com.tang.kite.datasource.unpooled

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel

/**
 * @author Tang
 */
data class UnpooledProperties(

    val driver: String,

    val url: String,

    val username: String? = null,

    val password: String? = null,

    var transactionIsolation: TransactionIsolationLevel? = null,

    var networkTimeout: Int? = null

)
