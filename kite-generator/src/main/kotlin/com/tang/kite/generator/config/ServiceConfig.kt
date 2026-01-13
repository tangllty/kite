package com.tang.kite.generator.config

import kotlin.reflect.KClass

/**
 * Service configuration
 *
 * @author Tang
 */
data class ServiceConfig(

    var enable: Boolean = true,

    var packagePath: String = "service",

    var superClass: KClass<*>? = null,

    var withImpl: Boolean = true,

    var implSuperClass: KClass<*>? = null

) {

    fun setSuperClass(superClass: Class<*>?) {
        this.superClass = superClass?.kotlin
    }

    fun setImplSuperClass(implSuperClass: Class<*>?) {
        this.implSuperClass = implSuperClass?.kotlin
    }

}
