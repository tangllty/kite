package com.tang.kite.datasource.pooled

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel

data class PooledProperties(

    val driver: String,

    val url: String,

    val username: String? = null,

    val password: String? = null,

    var transactionIsolation: TransactionIsolationLevel? = null,

    var networkTimeout: Int? = null,

    var initialConnections: Int = 5,

    var maximumActiveConnections: Int = 10,

    var minimumFreeConnections: Int = 2,

    var maximumFreeConnections: Int = 5

)
