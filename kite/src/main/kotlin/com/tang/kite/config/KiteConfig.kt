package com.tang.kite.config

import com.tang.kite.annotation.fill.CreateTime
import com.tang.kite.annotation.fill.UpdateTime
import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.enumeration.SqlType
import com.tang.kite.handler.fill.TimeFillHandler
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.sql.provider.SqlProvider
import com.tang.kite.sql.provider.derby.DerbySqlProvider
import com.tang.kite.sql.provider.h2.H2SqlProvider
import com.tang.kite.sql.provider.mysql.MysqlSqlProvider
import com.tang.kite.sql.provider.postgresql.PostgresqlSqlProvider
import com.tang.kite.sql.provider.sqlite.SqliteSqlProvider
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
     * The batch size for operations like inserts or updates.
     */
    @JvmStatic
    var batchSize = 1000

    /**
     * The strategy for selective query with a default value.
     */
    @JvmStatic
    var urlProviders: MutableMap<String, SqlProvider> = mutableMapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "sqlite" to SqliteSqlProvider(),
        "derby" to DerbySqlProvider(),
        "h2" to H2SqlProvider()
    )

    /**
     * Fill handlers for handling fill annotations.
     */
    @JvmStatic
    var fillHandlers: MutableMap<FillKey, FillHandler> = mutableMapOf(
        FillKey(CreateTime::class, SqlType.INSERT) to TimeFillHandler(),
        FillKey(UpdateTime::class, SqlType.UPDATE) to TimeFillHandler()
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
