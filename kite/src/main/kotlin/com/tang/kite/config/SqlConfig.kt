package com.tang.kite.config

import kotlin.time.DurationUnit

/**
 * SQL properties for SQL configuration.
 *
 * @author Tang
 */
object SqlConfig {

    /**
     * SQL lowercase setting.
     */
    @JvmStatic
    var sqlLowercase = true

    /**
     * Whether to log SQL statements.
     */
    @JvmStatic
    var sqlLogging = true

    /**
     * Whether to log SQL duration.
     */
    @JvmStatic
    var sqlDurationLogging = true

    /**
     * SQL duration unit.
     */
    @JvmStatic
    var durationUnit = DurationUnit.MILLISECONDS

    /**
     * SQL duration decimals.
     */
    @JvmStatic
    var durationDecimals = 0

    @JvmStatic
    fun getSql(sql: StringBuilder): String {
        return getSql(sql.toString())
    }

    @JvmStatic
    fun getSql(sql: String): String {
        return if (sqlLowercase) {
            sql.lowercase()
        } else {
            sql.uppercase()
        }
    }

}
