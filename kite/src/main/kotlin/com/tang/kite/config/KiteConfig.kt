package com.tang.kite.config

import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.sql.provider.derby.DerbySqlProvider
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import com.tang.kite.sql.provider.postgresql.PostgresqlSqlProvider
import java.util.function.Function

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
        "derby" to DerbySqlProvider()
    )

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
