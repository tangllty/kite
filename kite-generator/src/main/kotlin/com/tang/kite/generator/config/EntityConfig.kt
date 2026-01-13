package com.tang.kite.generator.config

import kotlin.reflect.KClass

/**
 * Entity configuration
 *
 * @author Tang
 */
data class EntityConfig(

    var enable: Boolean = true,

    var packagePath: String = "entity",

    var superClass: KClass<*>? = null,

    var withSerialVersionUID: Boolean = true,

    var withTableAnnotation: Boolean = false,

    var withColumnAnnotation: Boolean = false

) {

    fun setSuperClass(superClass: Class<*>?) {
        this.superClass = superClass?.kotlin
    }

}
