package com.tang.kite.boot.autoconfigure

import com.tang.kite.enumeration.transaction.TransactionIsolationLevel
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Data source properties for data source configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = DataSourceProperties.DATA_SOURCE_PREFIX)
open class DataSourceProperties (

    /**
     * Driver class name
     */
    var driver: String? = null,

    /**
     * URL
     */
    var url: String? = null,

    /**
     * Username
     */
    var username: String? = null,

    /**
     * Password
     */
    var password: String? = null,

    /**
     * Transaction isolation level
     */
    var transactionIsolation: TransactionIsolationLevel? = null,

    /**
     * Network timeout
     */
    var networkTimeout: Int? = null,

    /**
     * Initial connections
     */
    var initialConnections: Int = 5,

    /**
     * Maximum active connections
     */
    var maximumActiveConnections: Int = 10,

    /**
     * Minimum free connections
     */
    var minimumFreeConnections: Int = 2,

    /**
     * Maximum free connections
     */
    var maximumFreeConnections: Int = 5

) {

    companion object {

        const val DATA_SOURCE_PREFIX = KiteProperties.KITE_PREFIX + ".datasource"

    }

}
