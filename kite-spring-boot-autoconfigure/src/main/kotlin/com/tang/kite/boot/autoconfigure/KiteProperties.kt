package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.table.TableConfig
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
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

) {

    companion object {

        const val KITE_PREFIX = "kite"

    }

    fun apply() {
        KiteConfig.banner = banner
        KiteConfig.selectiveStrategy = selectiveStrategy
        KiteConfig.batchSize = batchSize
        KiteConfig.dialects = dialects
        KiteConfig.fillHandlers = fillHandlers

        PageConfig.pageNumber = page.pageNumber
        PageConfig.pageSize = page.pageSize
        PageConfig.pageNumberParameter = page.pageNumberParameter
        PageConfig.pageSizeParameter = page.pageSizeParameter

        SqlConfig.sqlLowercase = sql.sqlLowercase
        SqlConfig.sqlLogging = sql.sqlLogging
        SqlConfig.durationLogging = sql.durationLogging
        SqlConfig.durationUnit = sql.durationUnit
        SqlConfig.durationDecimals = sql.durationDecimals
        SqlConfig.prepareLogging = sql.prepareLogging
        SqlConfig.prepareUnit = sql.prepareUnit
        SqlConfig.prepareDecimals = sql.prepareDecimals
        SqlConfig.executionLogging = sql.executionLogging
        SqlConfig.executionUnit = sql.executionUnit
        SqlConfig.executionDecimals = sql.executionDecimals
        SqlConfig.mappingLogging = sql.mappingLogging
        SqlConfig.mappingUnit = sql.mappingUnit
        SqlConfig.mappingDecimals = sql.mappingDecimals
        SqlConfig.elapsedLogging = sql.elapsedLogging
        SqlConfig.elapsedUnit = sql.elapsedUnit
        SqlConfig.elapsedDecimals = sql.elapsedDecimals
        SqlConfig.sqlParser = sql.sqlParser
        SqlConfig.expressionMethods = sql.expressionMethods

        TableConfig.dynamicTableProcessor = table.dynamicTableProcessor

        LogicalDeletionConfig.enabled = logicalDeletion.enabled
        LogicalDeletionConfig.fieldName = logicalDeletion.fieldName
        LogicalDeletionConfig.logicalDeletionProcessor = logicalDeletion.logicalDeletionProcessor

        TenantConfig.enabled = tenant.enabled
        TenantConfig.fieldName = tenant.fieldName
        TenantConfig.tenantProcessor = tenant.tenantProcessor
    }

}
