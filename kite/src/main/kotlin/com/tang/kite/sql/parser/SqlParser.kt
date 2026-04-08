package com.tang.kite.sql.parser

import com.tang.kite.annotation.Param
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects
import java.lang.reflect.Parameter

/**
 * SQL parser interface.
 *
 * @author Tang
 */
interface SqlParser {

    /**
     * Parses SQL with parameters and optional beautification.
     *
     * @param sql SQL template string with placeholders and conditional logic
     * @param params Parameter map containing values for placeholders
     * @param beautify Whether to beautify the resulting SQL
     * @return Parsed and prepared SQL statement with parameter list
     * @throws IllegalArgumentException if SQL is blank or invalid
     */
    fun parse(sql: String, params: Map<String, Any?>, beautify: Boolean): SqlStatement

    /**
     * Parses SQL with parameters and default beautification.
     *
     * @param sql SQL template string with placeholders and conditional logic
     * @param params Parameter map containing values for placeholders
     * @return Parsed and prepared SQL statement with parameter list
     * @throws IllegalArgumentException if SQL is blank or invalid
     */
    fun parse(sql: String, params: Map<String, Any?>): SqlStatement {
        return parse(sql, params, true)
    }

    /**
     * Builds a comprehensive parameter map from method parameters and arguments.
     *
     * Supports multiple parameter naming strategies:
     * 1. @Param annotation: explicitly named parameters
     * 2. Indexed parameters: param1, param2, param3, etc.
     * 3. Single Map argument: all map entries are added
     * 4. Single object argument: all object fields are reflectively extracted
     *
     * @param parameters Method parameter metadata
     * @param args Actual argument values passed to the method
     * @return Map of parameter names to their values
     */
    fun buildParams(parameters: Array<Parameter>, args: Array<out Any>?): Map<String, Any?> {
        if (args.isNullOrEmpty()) {
            return emptyMap()
        }

        val map = mutableMapOf<String, Any?>()

        // Add indexed and annotated parameters
        parameters.forEachIndexed { index, param ->
            val annotation = param.getAnnotation(Param::class.java)
            if (annotation != null) {
                map[annotation.value] = args[index]
            }
            // Always add indexed parameter format
            map["param${index + 1}"] = args[index]
        }

        // Special handling for single argument
        if (args.size == 1) {
            when (val singleArg = args.first()) {
                is Map<*, *> -> {
                    // Merge all String-keyed map entries
                    singleArg.forEach { (key, value) ->
                        if (key is String) {
                            map[key] = value
                        }
                    }
                }
                is Any -> {
                    // Handle entity objects: extract all fields via reflection
                    if (Reflects.isEntity(singleArg)) {
                        val clazz = singleArg.javaClass
                        val fields = Reflects.getFields(clazz)
                        fields.forEach { field ->
                            Reflects.makeAccessible(field, singleArg)
                            map[field.name] = Reflects.getValue(singleArg, field.name)
                        }
                    }
                }
            }
        }
        return map
    }

}
