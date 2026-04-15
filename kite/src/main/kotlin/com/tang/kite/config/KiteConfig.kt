package com.tang.kite.config

import com.tang.kite.annotation.fill.CreateTime
import com.tang.kite.annotation.fill.UpdateTime
import com.tang.kite.config.defaults.DefaultSelectiveStrategy
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.table.TableConfig
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.enumeration.SqlType
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.handler.fill.TimeFillHandler
import com.tang.kite.sql.dialect.SqlDialect

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
    var dialects: MutableMap<String, SqlDialect> = mutableMapOf()

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

    /**
     * Logical deletion properties for logical deletion configuration.
     */
    @JvmStatic
    val logicalDelete = LogicalDeletionConfig

    /**
     * Tenant properties for tenant configuration.
     */
    @JvmStatic
    val tenant = TenantConfig

}
