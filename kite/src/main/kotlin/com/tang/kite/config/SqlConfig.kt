package com.tang.kite.config

import com.tang.kite.sql.parser.SqlParser
import com.tang.kite.sql.parser.defaults.DefaultSqlParser
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
    var durationLogging = true

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

    /**
     * Whether to log SQL prepare.
     */
    @JvmStatic
    var prepareLogging = durationLogging

    /**
     * SQL prepare unit.
     */
    @JvmStatic
    var prepareUnit = durationUnit

    /**
     * SQL prepare decimals.
     */
    @JvmStatic
    var prepareDecimals = durationDecimals

    /**
     * Whether to log SQL execution.
     */
    @JvmStatic
    var executionLogging = durationLogging

    /**
     * SQL execution unit.
     */
    @JvmStatic
    var executionUnit = durationUnit

    /**
     * SQL execution decimals.
     */
    @JvmStatic
    var executionDecimals = durationDecimals

    /**
     * Whether to log SQL mapping.
     */
    @JvmStatic
    var mappingLogging = durationLogging

    /**
     * SQL mapping unit.
     */
    @JvmStatic
    var mappingUnit = durationUnit

    /**
     * SQL mapping decimals.
     */
    @JvmStatic
    var mappingDecimals = durationDecimals

    /**
     * Whether to log SQL elapsed.
     */
    @JvmStatic
    var elapsedLogging = durationLogging

    /**
     * SQL elapsed unit.
     */
    @JvmStatic
    var elapsedUnit = durationUnit

    /**
     * SQL elapsed decimals.
     */
    @JvmStatic
    var elapsedDecimals = durationDecimals

    @JvmStatic
    var sqlParser: SqlParser = DefaultSqlParser

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
