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
class KiteConfig {

    var banner = true

    var pageNumber = 1L

    var pageSize = 10L

    var pageNumberParameter = "pageNumber"

    var pageSizeParameter = "pageSize"

    var selectiveStrategy = (Function<Any?, Boolean> { DefaultSelectiveStrategy.isSelective(it) })

    var sqlLowercase = true

    val urlProviders = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider(),
        "h2" to H2SqlProvider()
    )

    var enableSqlLogging = true

    var enableSqlDurationLogging = true

    var durationUnit = DurationUnit.MILLISECONDS

    var durationDecimals = 0

    fun getSql(sql: StringBuilder): String {
        return getSql(sql.toString())
    }

    fun getSql(sql: String): String {
        return if (sqlLowercase) {
            sql.lowercase()
        } else {
            sql.uppercase()
        }
    }

    companion object {

        val INSTANCE = KiteConfig()

    }

}
