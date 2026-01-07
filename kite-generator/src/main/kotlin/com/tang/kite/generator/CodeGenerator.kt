package com.tang.kite.generator

import com.tang.kite.generator.config.GeneratorConfig
import com.tang.kite.generator.database.DatabaseMetadataReader
import com.tang.kite.generator.info.TableInfo
import com.tang.kite.generator.template.TemplateManager
import com.tang.kite.logging.LOGGER
import java.io.File
import javax.sql.DataSource

/**
 * Kite code generator
 *
 * @author Tang
 */
class CodeGenerator(

    private val dataSource: DataSource,

    private val config: GeneratorConfig

) {

    /**
     * Generate code
     */
    fun generate() {
        val metadataReader = DatabaseMetadataReader(dataSource, config)
        val tables = metadataReader.getTables()

        // Filter tables with prefix
        val filteredTables = if (config.tablePrefix.isNotEmpty()) {
            tables.filter { it.tableName.startsWith(config.tablePrefix) }
        } else {
            tables
        }

        if (filteredTables.isEmpty()) {
            LOGGER.warn("No matching tables found for code generation")
            return
        }

        filteredTables.forEach { tableInfo ->
            generateEntity(tableInfo)
            if (config.mapperConfig.enable) {
                generateMapper(tableInfo)
            }
            if (config.serviceConfig.enable) {
                generateService(tableInfo)
                if (config.serviceConfig.withImpl) {
                    generateServiceImpl(tableInfo)
                }
            }
            if (config.controllerConfig.enable) {
                generateController(tableInfo)
            }
        }
    }

    private fun generateEntity(tableInfo: TableInfo) {
        if (!config.entityConfig.enable) return

        val templateName = getTemplateName("entity")

        val dataModel = mapOf(
            "table" to tableInfo,
            "config" to config,
            "entity" to config.entityConfig
        )

        val code = TemplateManager.processTemplate(templateName, dataModel)
        val entityDir = getOutputDir(config.entityConfig.packagePath)
        val fileName = tableInfo.className + config.language.extension
        val entityFile = File(entityDir, fileName)

        writeToFile(entityFile, code)
    }

    private fun generateMapper(tableInfo: TableInfo) {
        val templateName = getTemplateName("mapper")

        val dataModel = mapOf(
            "table" to tableInfo,
            "config" to config,
            "entity" to config.entityConfig,
            "mapper" to config.mapperConfig
        )

        val code = TemplateManager.processTemplate(templateName, dataModel)
        val mapperDir = getOutputDir(config.mapperConfig.packagePath)
        val fileName = "${tableInfo.className}Mapper${config.language.extension}"
        val mapperFile = File(mapperDir, fileName)

        writeToFile(mapperFile, code)
    }

    private fun generateService(tableInfo: TableInfo) {
        val templateName = getTemplateName("service")

        val dataModel = mapOf(
            "table" to tableInfo,
            "config" to config,
            "entity" to config.entityConfig,
            "service" to config.serviceConfig
        )

        val code = TemplateManager.processTemplate(templateName, dataModel)
        val serviceDir = getOutputDir(config.serviceConfig.packagePath)
        val fileName = "${tableInfo.className}Service${config.language.extension}"
        val serviceFile = File(serviceDir, fileName)

        writeToFile(serviceFile, code)
    }

    private fun generateServiceImpl(tableInfo: TableInfo) {
        val templateName = getTemplateName("serviceImpl")

        val dataModel = mapOf(
            "table" to tableInfo,
            "config" to config,
            "entity" to config.entityConfig,
            "mapper" to config.mapperConfig,
            "service" to config.serviceConfig
        )

        val code = TemplateManager.processTemplate(templateName, dataModel)
        val serviceImplDir = getOutputDir("${config.serviceConfig.packagePath}/impl")
        val fileName = "${tableInfo.className}ServiceImpl${config.language.extension}"
        val serviceImplFile = File(serviceImplDir, fileName)

        writeToFile(serviceImplFile, code)
    }

    private fun generateController(tableInfo: TableInfo) {
        val templateName = getTemplateName("controller")

        val dataModel = mapOf(
            "table" to tableInfo,
            "config" to config,
            "entity" to config.entityConfig,
            "service" to config.serviceConfig,
            "controller" to config.controllerConfig
        )

        val code = TemplateManager.processTemplate(templateName, dataModel)
        val controllerDir = getOutputDir(config.controllerConfig.packagePath)
        val fileName = "${tableInfo.className}Controller${config.language.extension}"
        val controllerFile = File(controllerDir, fileName)

        writeToFile(controllerFile, code)
    }

    private fun getTemplateName(name: String): String {
        return name + config.language.extension + ".ftl"
    }

    private fun getOutputDir(packagePath: String): File {
        val module = config.module
        val language = config.language.name.lowercase()
        val packageName = (config.packageName + "." + packagePath).replace(".", "/")
        val outputDir = File(config.rootDir, module + "/src/main/${language}/${packageName}")
        outputDir.mkdirs()
        return outputDir
    }

    private fun writeToFile(file: File, content: String) {
        file.parentFile.mkdirs()
        file.writeText(content, Charsets.UTF_8)
        LOGGER.info("Code generated successfully: ${file.absolutePath}")
    }

}
