package com.tang.kite.generator.config

import kotlin.reflect.KClass

/**
 * Controller configuration
 *
 * @author Tang
 */
data class ControllerConfig(

    var enable: Boolean = true,

    var packagePath: String = "controller",

    var superClass: KClass<*>? = null

) {

    fun setSuperClass(superClass: Class<*>?) {
        this.superClass = superClass?.kotlin
    }

}
