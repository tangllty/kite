package com.tang.kite.generator.template

import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.MultiTemplateLoader
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File
import java.io.StringWriter

/**
 * Template manager
 *
 * @author Tang
 */
object TemplateManager {

    private val configuration: Configuration = Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).apply {
        // Set template loader, prioritize file system loading, then classpath loading
        val fileLoader = try {
            FileTemplateLoader(File("templates"))
        } catch (_: Exception) {
            null
        }

        val classLoader = ClassTemplateLoader(this::class.java.classLoader, "/templates")

        // Use MultiTemplateLoader to support both file system and classpath templates
        templateLoader = if (fileLoader != null) {
            MultiTemplateLoader(arrayOf(fileLoader, classLoader))
        } else {
            classLoader
        }

        templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        logTemplateExceptions = false
        wrapUncheckedExceptions = true
        fallbackOnNullLoopVariable = false
    }

    /**
     * Process template
     */
    fun processTemplate(templateName: String, dataModel: Map<String, Any>): String {
        val template = configuration.getTemplate(templateName)
        val stringWriter = StringWriter()
        template.process(dataModel, stringWriter)
        return stringWriter.toString()
    }

}
