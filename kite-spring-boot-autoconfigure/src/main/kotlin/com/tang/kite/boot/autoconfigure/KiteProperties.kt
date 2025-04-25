package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.function.Function
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.time.DurationUnit

/**
 * Kite properties
 *
 * @author Tang
 */
@ConfigurationProperties(prefix = KiteProperties.KITE_PREFIX)
data class KiteProperties(

    val banner: Boolean = KiteConfig.banner,

    /**
     * The strategy for selective query
     */
    val selectiveStrategy: Function<Any?, Boolean> = KiteConfig.selectiveStrategy,

    val urlProviders: Map<String, Any> = KiteConfig.urlProviders,

    val page: PageProperties = PageProperties(),

    val sql: SqlProperties = SqlProperties()

) {

    data class PageProperties (

        val pageNumber: Long = PageConfig.pageNumber,

        val pageSize: Long = PageConfig.pageSize,

        val pageNumberParameter: String = PageConfig.pageNumberParameter,

        val pageSizeParameter: String = PageConfig.pageSizeParameter

    )

    data class SqlProperties (

        var sqlLowercase: Boolean = SqlConfig.sqlLowercase,

        var sqlLogging: Boolean = SqlConfig.sqlLogging,

        var sqlDurationLogging: Boolean = SqlConfig.sqlDurationLogging,

        var durationUnit: DurationUnit = SqlConfig.durationUnit,

        var durationDecimals: Int = SqlConfig.durationDecimals

    )

    companion object {

        const val KITE_PREFIX = "kite"

    }

    fun apply() {
        val fields = this.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val value = field.get(this)
            val instance = KiteConfig
            val properties = KiteConfig::class.memberProperties
            properties.filter {
                it.name == field.name
            }.forEach {
                if (it is KMutableProperty1) {
                    it.setter.call(instance, value)
                }
            }
        }
    }

}
