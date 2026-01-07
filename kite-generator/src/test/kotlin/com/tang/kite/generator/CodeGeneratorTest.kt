package com.tang.kite.generator

import com.tang.kite.generator.config.GeneratorConfig
import com.tang.kite.generator.config.Language
import com.tang.kite.generator.config.ControllerConfig
import com.tang.kite.generator.config.EntityConfig
import com.tang.kite.generator.config.MapperConfig
import com.tang.kite.generator.config.ServiceConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Code generator test
 *
 * @author Tang
 */
class CodeGeneratorTest : BaseDataTest() {

    private val dataSource = createDataSource()

    @Test
    fun testGenerateKotlinCode() {
        testGenerateCode(Language.KOTLIN)
    }

    @Test
    fun testGenerateJavaCode() {
        testGenerateCode(Language.JAVA)
    }

    private fun testGenerateCode(language: Language) {
        val config = GeneratorConfig(
            packageName = "com.test.project",
            rootDir = System.getProperty("user.dir").substringBeforeLast("/").substringBeforeLast("\\"),
            module = "kite-generator-test",
            author = "Tang",
            entityConfig = EntityConfig(),
            mapperConfig = MapperConfig(),
            serviceConfig = ServiceConfig(),
            controllerConfig = ControllerConfig(),
            tableNames = listOf("account", "user"),
            language = language
        )

        val generator = CodeGenerator(dataSource, config)
        generator.generate()

        val languageFolder = language.name.lowercase()

        val generatedFiles = listOf(
            "${config.rootDir}/${config.module}/src/main/$languageFolder/${config.packageName.replace('.', '/')}/entity/Account${language.extension}",
            "${config.rootDir}/${config.module}/src/main/$languageFolder/${config.packageName.replace('.', '/')}/mapper/AccountMapper${language.extension}",
            "${config.rootDir}/${config.module}/src/main/$languageFolder/${config.packageName.replace('.', '/')}/service/AccountService${language.extension}",
            "${config.rootDir}/${config.module}/src/main/$languageFolder/${config.packageName.replace('.', '/')}/service/impl/AccountServiceImpl${language.extension}",
            "${config.rootDir}/${config.module}/src/main/$languageFolder/${config.packageName.replace('.', '/')}/controller/AccountController${language.extension}",
        )

        generatedFiles.forEach { filePath ->
            assertTrue("File should exist: $filePath") {
                File(filePath).exists()
            }
        }
    }

}
