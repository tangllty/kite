package com.tang.kite.generator.config

/**
 * Controller configuration
 *
 * @author Tang
 */
data class ControllerConfig(

    var enable: Boolean = true,

    var packagePath: String = "controller",

    var superClass: String? = null

)
