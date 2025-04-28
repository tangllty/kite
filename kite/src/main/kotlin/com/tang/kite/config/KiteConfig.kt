package com.tang.kite.config

import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.sql.provider.SqlProvider
import com.tang.kite.sql.provider.derby.DerbySqlProvider
import com.tang.kite.sql.provider.h2.H2SqlProvider
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import com.tang.kite.sql.provider.postgresql.PostgresqlSqlProvider
import java.util.function.Function

/**
 * Kite properties class includes core properties for Kite framework.
 *
 * @author Tang
 */
object KiteConfig {

    /**
     * Whether to display the banner during application startup.
     */
    @JvmStatic
    var banner = true

    /**
     * The strategy for selective query
     */
    @JvmStatic
    var selectiveStrategy = (Function<Any?, Boolean> { DefaultSelectiveStrategy.isSelective(it) })

    /**
     * The strategy for selective query with a default value.
     */
    @JvmStatic
    var urlProviders: Map<String, SqlProvider> = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider(),
        "h2" to H2SqlProvider()
    )

    /**
     * Page properties for pagination configuration.
     */
    @JvmStatic
    val page = PageConfig

    /**
     * SQL properties for SQL configuration.
     */
    @JvmStatic
    val sql = SqlConfig

}
