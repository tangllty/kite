package com.tang.kite.datasource.unpooled

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel

/**
 * @author Tang
 */
open class UnpooledProperties {

    var driver: String? = null

    var url: String? = null

    var username: String? = null

    var password: String? = null

    var transactionIsolation: TransactionIsolationLevel? = null

    var networkTimeout: Int? = null

}
