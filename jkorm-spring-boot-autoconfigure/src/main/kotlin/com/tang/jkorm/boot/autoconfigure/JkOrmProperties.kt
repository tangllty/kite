package com.tang.jkorm.boot.autoconfigure

import com.tang.jkorm.config.JkOrmConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.reflect.full.memberProperties

/**
 * JkOrm properties
 *
 * @author Tang
 */
@ConfigurationProperties(prefix = JkOrmProperties.JKORM_PREFIX)
open class JkOrmProperties {

    companion object {

        const val JKORM_PREFIX = "jkorm"

    }

    var banner: Boolean = JkOrmConfig.INSTANCE.banner

    var pageNumber: Long = JkOrmConfig.INSTANCE.pageNumber

    var pageSize: Long = JkOrmConfig.INSTANCE.pageSize

    var pageNumberParameter: String = JkOrmConfig.INSTANCE.pageNumberParameter

    var pageSizeParameter: String = JkOrmConfig.INSTANCE.pageSizeParameter

    var selectiveStrategy: Function<Any?, Boolean> = JkOrmConfig.INSTANCE.selectiveStrategy

    var sqlLowercase: Boolean = JkOrmConfig.INSTANCE.sqlLowercase

    fun apply() {
        val fields = this.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val value = field.get(this)
            val instance = JkOrmConfig.INSTANCE
            val properties = JkOrmConfig::class.memberProperties
            properties.filter {
                it.name == field.name
            }.forEach {
                if (it is kotlin.reflect.KMutableProperty1) {
                    it.setter.call(instance, value)
                }
            }
        }
    }

}
