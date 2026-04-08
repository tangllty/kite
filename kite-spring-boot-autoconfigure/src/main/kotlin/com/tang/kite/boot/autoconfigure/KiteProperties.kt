package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.logical.LogicalDeletionProcessor
import com.tang.kite.config.table.DynamicTableProcessor
import com.tang.kite.config.table.TableConfig
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.config.tenant.TenantProcessor
import com.tang.kite.expression.ExpressionMethod
import com.tang.kite.handler.fill.FillHandler
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.parser.SqlParser
import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.time.DurationUnit

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
    val banner: Boolean = KiteConfig.banner,

    /**
     * The strategy for selective query
     */
    val selectiveStrategy: Function1<Any?, Boolean> = KiteConfig.selectiveStrategy,

    /**
     * The batch size for operations like inserts or updates.
     */
    var batchSize: Int = KiteConfig.batchSize,

    /**
     * SQL dialects for different databases.
     */
    var dialects: MutableMap<DatabaseType, SqlDialect> = KiteConfig.dialects,

    /**
     * Fill handlers for handling fill annotations.
     */
    val fillHandlers: MutableMap<FillKey, FillHandler> = KiteConfig.fillHandlers,

    /**
     * Page properties for pagination configuration.
     */
    val page: PageProperties = PageProperties(),

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
    val logicalDelete: LogicalDeleteProperties = LogicalDeleteProperties(),

    /**
     * Tenant properties for tenant configuration.
     */
    val tenant: TenantProperties = TenantProperties()

) {

    /**
     * Page properties for pagination configuration.
     */
    class PageProperties (

        /**
         * Default page number.
         */
        val pageNumber: Long = PageConfig.pageNumber,

        /**
         * Default page size.
         */
        val pageSize: Long = PageConfig.pageSize,

        /**
         * Default page number parameter name.
         */
        val pageNumberParameter: String = PageConfig.pageNumberParameter,

        /**
         * Default page size parameter name.
         */
        val pageSizeParameter: String = PageConfig.pageSizeParameter

    )

    /**
     * SQL properties for SQL configuration.
     */
    class SqlProperties (

        /**
         * SQL lowercase setting.
         */
        var sqlLowercase: Boolean = SqlConfig.sqlLowercase,

        /**
         * Whether to log SQL statements.
         */
        var sqlLogging: Boolean = SqlConfig.sqlLogging,

        /**
         * Whether to log SQL duration.
         */
        var durationLogging: Boolean = SqlConfig.durationLogging,

        /**
         * SQL duration unit.
         */
        var durationUnit: DurationUnit = SqlConfig.durationUnit,

        /**
         * SQL duration decimals.
         */
        var durationDecimals: Int = SqlConfig.durationDecimals,

        /**
         * Whether to log SQL prepare.
         */
        var prepareLogging: Boolean = SqlConfig.prepareLogging,

        /**
         * SQL prepare unit.
         */
        var prepareUnit: DurationUnit = SqlConfig.prepareUnit,

        /**
         * SQL prepare decimals.
         */
        var prepareDecimals: Int = SqlConfig.prepareDecimals,

        /**
         * Whether to log SQL execution.
         */
        var executionLogging: Boolean = SqlConfig.executionLogging,

        /**
         * SQL execution unit.
         */
        var executionUnit: DurationUnit = SqlConfig.executionUnit,

        /**
         * SQL execution decimals.
         */
        var executionDecimals: Int = SqlConfig.executionDecimals,

        /**
         * Whether to log SQL mapping.
         */
        var mappingLogging: Boolean = SqlConfig.mappingLogging,

        /**
         * SQL mapping unit.
         */
        var mappingUnit: DurationUnit = SqlConfig.mappingUnit,

        /**
         * SQL mapping decimals.
         */
        var mappingDecimals: Int = SqlConfig.mappingDecimals,

        /**
         * Whether to log SQL elapsed.
         */
        var elapsedLogging: Boolean = SqlConfig.elapsedLogging,

        /**
         * SQL elapsed unit.
         */
        var elapsedUnit: DurationUnit = SqlConfig.elapsedUnit,

        /**
         * SQL elapsed decimals.
         */
        var elapsedDecimals: Int = SqlConfig.elapsedDecimals,

        /**
         * SQL parser.
         */
        var sqlParser: SqlParser = SqlConfig.sqlParser,

        /**
         * Expression methods for SQL configuration.
         */
        var expressionMethods: MutableMap<String, ExpressionMethod> = SqlConfig.expressionMethods

    )

    /**
     * Table properties for table configuration.
     */
    class TableProperties (

        /**
         * Dynamic table name processor.
         */
        var dynamicTableProcessor: DynamicTableProcessor? = TableConfig.dynamicTableProcessor

    )

    class LogicalDeleteProperties (

        /**
         * Whether logical delete is enabled
         */
        val enabled: Boolean = LogicalDeletionConfig.enabled,

        /**
         * Logical delete field name
         */
        val fieldName: String = LogicalDeletionConfig.fieldName,

        /**
         * Static processor for logical delete operations
         */
        var logicalDeletionProcessor: LogicalDeletionProcessor = LogicalDeletionConfig.logicalDeletionProcessor

    )

    /**
     * Tenant properties for tenant configuration.
     */
    class TenantProperties (

        /**
         * Whether tenant functionality is enabled
         */
        val enabled: Boolean = TenantConfig.enabled,

        /**
         * Tenant ID field name
         */
        val fieldName: String = TenantConfig.fieldName,

        /**
         * Tenant processor for handling multiple tenant IDs
         */
        var tenantProcessor: TenantProcessor = TenantConfig.tenantProcessor

    )

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

        LogicalDeletionConfig.enabled = logicalDelete.enabled
        LogicalDeletionConfig.fieldName = logicalDelete.fieldName
        LogicalDeletionConfig.logicalDeletionProcessor = logicalDelete.logicalDeletionProcessor

        TenantConfig.enabled = tenant.enabled
        TenantConfig.fieldName = tenant.fieldName
        TenantConfig.tenantProcessor = tenant.tenantProcessor
    }

}
