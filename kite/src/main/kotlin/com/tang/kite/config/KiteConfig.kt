package com.tang.kite.config

import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.sql.provider.derby.DerbySqlProvider
import com.tang.kite.sql.provider.h2.H2SqlProvider
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import com.tang.kite.sql.provider.postgresql.PostgresqlSqlProvider
import java.util.function.Function

/**
 * Kite configuration
 *
 * @author Tang
 */
object KiteConfig {

    @JvmStatic
    var banner = true

    @JvmStatic
    var selectiveStrategy = (Function<Any?, Boolean> { DefaultSelectiveStrategy.isSelective(it) })

    @JvmStatic
    val urlProviders = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider(),
        "h2" to H2SqlProvider()
    )

    @JvmStatic
    val page = PageConfig

    @JvmStatic
    val sql = SqlConfig

}
