package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.function.Function
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

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

    var banner: Boolean = KiteConfig.INSTANCE.banner

    var pageNumber: Long = KiteConfig.INSTANCE.pageNumber

    var pageSize: Long = KiteConfig.INSTANCE.pageSize

    var pageNumberParameter: String = KiteConfig.INSTANCE.pageNumberParameter

    var pageSizeParameter: String = KiteConfig.INSTANCE.pageSizeParameter

    var selectiveStrategy: Function<Any?, Boolean> = KiteConfig.INSTANCE.selectiveStrategy

    var sqlLowercase: Boolean = KiteConfig.INSTANCE.sqlLowercase

    var urlProviders: Map<String, Any> = KiteConfig.INSTANCE.urlProviders

    var enableSqlLogging: Boolean = KiteConfig.INSTANCE.enableSqlLogging

    fun apply() {
        val fields = this.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val value = field.get(this)
            val instance = KiteConfig.INSTANCE
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
