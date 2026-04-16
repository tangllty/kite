package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.handler.result.ResultHandler
import com.tang.kite.sql.dialect.SqlDialect
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Kite properties class includes core properties for Kite framework.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
open class KiteProperties(

    /**
     * Whether to display the banner during application startup.
     */
    var banner: Boolean = KiteConfig.banner,

    /**
     * The strategy for selective query
     */
    var selectiveStrategy: Function1<Any?, Boolean> = KiteConfig.selectiveStrategy,

    /**
     * The batch size for operations like inserts or updates.
     */
    var batchSize: Int = KiteConfig.batchSize,

    /**
     * SQL dialects for different databases.
     */
    var dialects: MutableMap<String, SqlDialect> = KiteConfig.dialects,

    /**
     * Fill handlers for handling fill annotations.
     */
    var fillHandlers: MutableMap<FillKey, FillHandler> = KiteConfig.fillHandlers,

    /**
     * Result handlers for handling result types.
     */
    var resultHandlers: MutableMap<Class<*>, ResultHandler> = KiteConfig.resultHandlers,

    /**
     * Data source properties for data source configuration.
     */
    var datasource: LinkedHashMap<String, Any?> = LinkedHashMap(),

    /**
     * Page properties for pagination configuration.
     */
    var page: PageProperties = PageProperties(),

    /**
     * SQL properties for SQL configuration.
     */
    val sql: SqlProperties = SqlProperties(),

    /**
     * Table properties for table configuration.
     */
    val table: TableProperties = TableProperties(),

    /**
     * Logical delete properties for logical delete configuration.
     */
    val logicalDeletion: LogicalDeletionProperties = LogicalDeletionProperties(),

    /**
     * Tenant properties for tenant configuration.
     */
    val tenant: TenantProperties = TenantProperties()

) : PropertyApplier {

    companion object {

        const val KITE_PREFIX = "kite"

    }

    override fun applyProperties() {
        KiteConfig.banner = banner
        KiteConfig.selectiveStrategy = selectiveStrategy
        KiteConfig.batchSize = batchSize
        KiteConfig.dialects = dialects
        KiteConfig.fillHandlers = fillHandlers
        KiteConfig.resultHandlers = resultHandlers
        page.applyProperties()
        sql.applyProperties()
        table.applyProperties()
        logicalDeletion.applyProperties()
        tenant.applyProperties()
    }

}
