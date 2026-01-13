package com.tang.kite.generator.config

import kotlin.reflect.KClass

/**
 * Mapper configuration
 *
 * @author Tang
 */
data class MapperConfig(

    var enable: Boolean = true,

    var packagePath: String = "mapper",

    var superClass: KClass<*>? = null

) {

    fun setSuperClass(superClass: Class<*>?) {
        this.superClass = superClass?.kotlin
    }

}
