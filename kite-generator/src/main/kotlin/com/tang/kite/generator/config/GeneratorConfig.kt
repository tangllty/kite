package com.tang.kite.generator.config

/**
 * Code generator configuration
 *
 * @author Tang
 */
data class GeneratorConfig(

    var packageName: String = "",

    var rootDir: String = System.getProperty("user.dir"),

    var module: String = "",

    var author: String? = null,

    var entityConfig: EntityConfig = EntityConfig(),

    var mapperConfig: MapperConfig = MapperConfig(),

    var serviceConfig: ServiceConfig = ServiceConfig(),

    var controllerConfig: ControllerConfig = ControllerConfig(),

    var tablePrefix: String = "",

    var tableNames: List<String> = emptyList(),

    var language: Language = Language.KOTLIN

)
