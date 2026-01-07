package com.tang.kite.generator.config

/**
 * Mapper configuration
 *
 * @author Tang
 */
data class MapperConfig(

    var enable: Boolean = true,

    var packagePath: String = "mapper",

    var superClass: String? = null

)
