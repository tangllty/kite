package com.tang.kite.config

import com.tang.kite.annotation.fill.CreateTime
import com.tang.kite.annotation.fill.UpdateTime
import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.config.table.TableConfig
import com.tang.kite.enumeration.SqlType
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.handler.fill.TimeFillHandler
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.factory.defaults.DefaultSqlDialectFactory

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
     * The strategy for selective methods.
     */
    @JvmStatic
    var selectiveStrategy: Function1<Any?, Boolean> = DefaultSelectiveStrategy::isSelective

    /**
     * The batch size for operations like inserts or updates.
     */
    @JvmStatic
    var batchSize = 1000

    /**
     * SQL dialects for different databases.
     */
    @JvmStatic
    var dialects: MutableMap<DatabaseType, SqlDialect> = DefaultSqlDialectFactory().getDialects()

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

    /**
     * Table properties for table configuration.
     */
    @JvmStatic
    val table = TableConfig

}
