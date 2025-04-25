package com.tang.kite.config

import kotlin.time.DurationUnit

/**
 * @author Tang
 */
object SqlConfig {

    @JvmStatic
    var sqlLowercase = true

    @JvmStatic
    var sqlLogging = true

    @JvmStatic
    var sqlDurationLogging = true

    @JvmStatic
    var durationUnit = DurationUnit.MILLISECONDS

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
