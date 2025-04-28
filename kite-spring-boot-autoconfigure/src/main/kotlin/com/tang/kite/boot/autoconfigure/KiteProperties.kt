package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import com.tang.kite.sql.provider.SqlProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.function.Function
import kotlin.time.DurationUnit

/**
 * Kite properties class includes core properties for Kite framework.
 *
 * @author Tang
 */
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
data class KiteProperties(

    /**
     * Whether to display the banner during application startup.
     */
    val banner: Boolean = KiteConfig.banner,

    /**
     * The strategy for selective query
     */
    val selectiveStrategy: Function<Any?, Boolean> = KiteConfig.selectiveStrategy,

    /**
     * The strategy for selective query with a default value.
     */
    val urlProviders: Map<String, SqlProvider> = KiteConfig.urlProviders,

    /**
     * Page properties for pagination configuration.
     */
    val page: PageProperties = PageProperties(),

    /**
     * SQL properties for SQL configuration.
     */
    val sql: SqlProperties = SqlProperties()

) {

    /**
     * Page properties for pagination configuration.
     */
    data class PageProperties (

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
    data class SqlProperties (

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
        var sqlDurationLogging: Boolean = SqlConfig.sqlDurationLogging,

        /**
         * SQL duration unit.
         */
        var durationUnit: DurationUnit = SqlConfig.durationUnit,

        /**
         * SQL duration decimals.
         */
        var durationDecimals: Int = SqlConfig.durationDecimals

    )

    companion object {

        const val KITE_PREFIX = "kite"

    }

    fun apply() {
        KiteConfig.banner = banner
        KiteConfig.selectiveStrategy = selectiveStrategy
        KiteConfig.urlProviders = urlProviders

        PageConfig.pageNumber = page.pageNumber
        PageConfig.pageSize = page.pageSize
        PageConfig.pageNumberParameter = page.pageNumberParameter
        PageConfig.pageSizeParameter = page.pageSizeParameter

        SqlConfig.sqlLowercase = sql.sqlLowercase
        SqlConfig.sqlLogging = sql.sqlLogging
        SqlConfig.sqlDurationLogging = sql.sqlDurationLogging
        SqlConfig.durationUnit = sql.durationUnit
        SqlConfig.durationDecimals = sql.durationDecimals
    }

}
