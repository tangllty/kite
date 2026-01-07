package com.tang.kite.generator.config

/**
 * Service configuration
 *
 * @author Tang
 */
data class ServiceConfig(

    var enable: Boolean = true,

    var packagePath: String = "service",

    var superClass: String? = null,

    var withImpl: Boolean = true

)
