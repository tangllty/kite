package com.tang.kite.generator

import com.tang.kite.generator.config.ControllerConfig
import com.tang.kite.generator.config.EntityConfig
import com.tang.kite.generator.config.GeneratorConfig
import com.tang.kite.generator.config.Language
import com.tang.kite.generator.config.MapperConfig
import com.tang.kite.generator.config.ServiceConfig

/**
 * Code generator builder
 *
 * @author Tang
 */
class GeneratorBuilder {

    private val config = GeneratorConfig()

    fun packageName(packageName: String) = apply {
        config.packageName = packageName
    }

    fun tablePrefix(tablePrefix: String) = apply {
        config.tablePrefix = tablePrefix
    }

    fun tableNames(vararg tableNames: String) = apply {
        config.tableNames = tableNames.toList()
    }

    fun tableNames(tableNames: List<String>) = apply {
        config.tableNames = tableNames
    }

    fun language(language: Language) = apply {
        config.language = language
    }

    fun entityConfig(config: EntityConfig) = apply {
        this.config.entityConfig = config
    }

    fun entityConfig(block: EntityConfig.() -> Unit) = apply {
        config.entityConfig = EntityConfig().apply(block)
    }

    fun mapperConfig(config: MapperConfig) = apply {
        this.config.mapperConfig = config
    }

    fun mapperConfig(block: MapperConfig.() -> Unit) = apply {
        config.mapperConfig = MapperConfig().apply(block)
    }

    fun serviceConfig(config: ServiceConfig) = apply {
        this.config.serviceConfig = config
    }

    fun serviceConfig(block: ServiceConfig.() -> Unit) = apply {
        config.serviceConfig = ServiceConfig().apply(block)
    }

    fun controllerConfig(config: ControllerConfig) = apply {
        this.config.controllerConfig = config
    }

    fun controllerConfig(block: ControllerConfig.() -> Unit) = apply {
        config.controllerConfig = ControllerConfig().apply(block)
    }

    fun build(): GeneratorConfig {
        return config
    }

}
