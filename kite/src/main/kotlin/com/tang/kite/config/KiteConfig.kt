package com.tang.kite.config

import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.sql.provider.derby.DerbySqlProvider
import com.tang.kite.sql.provider.h2.H2SqlProvider
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import com.tang.kite.sql.provider.postgresql.PostgresqlSqlProvider
import java.util.function.Function
import kotlin.time.DurationUnit

/**
 * Kite configuration
 *
 * @author Tang
 */
object KiteConfig {

    @JvmStatic
    var banner = true

    @JvmStatic
    var pageNumber = 1L

    @JvmStatic
    var pageSize = 10L

    @JvmStatic
    var pageNumberParameter = "pageNumber"

    @JvmStatic
    var pageSizeParameter = "pageSize"

    @JvmStatic
    var selectiveStrategy = (Function<Any?, Boolean> { DefaultSelectiveStrategy.isSelective(it) })

    @JvmStatic
    var sqlLowercase = true

    @JvmStatic
    val urlProviders = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider(),
        "h2" to H2SqlProvider()
    )

    @JvmStatic
    var enableSqlLogging = true

    @JvmStatic
    var enableSqlDurationLogging = true

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
