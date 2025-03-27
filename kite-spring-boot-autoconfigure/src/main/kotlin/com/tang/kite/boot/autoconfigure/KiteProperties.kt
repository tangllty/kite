package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
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
open class KiteProperties {

    companion object {

        const val KITE_PREFIX = "kite"

    }

    var banner: Boolean = KiteConfig.banner

    var pageNumber: Long = KiteConfig.pageNumber

    var pageSize: Long = KiteConfig.pageSize

    var pageNumberParameter: String = KiteConfig.pageNumberParameter

    var pageSizeParameter: String = KiteConfig.pageSizeParameter

    var selectiveStrategy: Function<Any?, Boolean> = KiteConfig.selectiveStrategy

    var sqlLowercase: Boolean = KiteConfig.sqlLowercase

    var urlProviders: Map<String, Any> = KiteConfig.urlProviders

    var enableSqlLogging: Boolean = KiteConfig.enableSqlLogging

    var enableSqlDurationLogging = true

    var durationUnit = DurationUnit.MILLISECONDS

    var durationDecimals = 0

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
