package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.SqlConfig
import com.tang.kite.expression.ExpressionMethod
import com.tang.kite.sql.parser.SqlParser
import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.time.DurationUnit

/**
 * SQL properties for SQL configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = SqlProperties.SQL_PREFIX)
open class SqlProperties (

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

) {

    companion object {

        const val SQL_PREFIX = KiteProperties.KITE_PREFIX + ".sql"

    }

}
