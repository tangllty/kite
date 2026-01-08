package com.tang.kite.generator.config

/**
 * Entity configuration
 *
 * @author Tang
 */
data class EntityConfig(

    var enable: Boolean = true,

    var packagePath: String = "entity",

    var superClass: String? = null,

    var withSerialVersionUID: Boolean = true,

    var withTableAnnotation: Boolean = false,

    var withColumnAnnotation: Boolean = false

)
