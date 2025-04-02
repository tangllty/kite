package com.tang.kite.io

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel

/**
 * @author Tang
 */
class TestKiteProperties {

    var datasource = TestDataSourceProperties()

}

class TestDataSourceProperties {

    var driver: String? = null

    var url: String? = null

    var username: String? = null

    var password: String? = null

    var transactionIsolation: TransactionIsolationLevel? = null

    var networkTimeout: Int? = null

}
