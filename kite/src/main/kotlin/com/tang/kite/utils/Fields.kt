package com.tang.kite.utils

import com.tang.kite.config.KiteConfig
import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.enumeration.SqlType
import com.tang.kite.function.SFunction
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.result.ResultHandlerFactory
import com.tang.kite.utils.Reflects.makeAccessible
import java.lang.invoke.SerializedLambda
import java.lang.reflect.Field
import java.util.Locale
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
object Fields {

    private fun <T> getSerializedLambda(function: SFunction<T, *>): SerializedLambda {
        val writeReplaceMethod = function.javaClass.getDeclaredMethod("writeReplace")
        makeAccessible(writeReplaceMethod, function)
        return writeReplaceMethod.invoke(function) as SerializedLambda
    }

    fun <T> getFieldName(function: SFunction<T, *>): String {
        val serializedLambda = getSerializedLambda(function)
        val methodPrefix = "get"
        val fieldName = serializedLambda.implMethodName.substring(methodPrefix.length)
        return fieldName.replaceFirst(
            fieldName[0].toString().toRegex(),
            fieldName[0].toString().lowercase(Locale.getDefault())
        )
    }

    fun <T> getFieldName(property: KProperty1<T, *>): String {
        return property.name
    }

    fun <T> getField(function: SFunction<T, *>): Field {
        val fieldName = getFieldName(function)
        val serializedLambda = getSerializedLambda(function)
        return Class.forName(serializedLambda.implClass.replace("/", ".")).getDeclaredField(fieldName)
    }

    fun <T> getField(property: KProperty1<T, *>): Field {
        val fieldName = getFieldName(property)
        return property.javaField ?: throw NoSuchFieldException("Field not found: $fieldName")
    }

    fun <T> setValue(field: Field, instance: T, value: Any?) {
        makeAccessible(field, instance)
        val resultHandler = ResultHandlerFactory().newResultHandler(field)
        if (value == null) {
            resultHandler.setNullValue(field, instance, value)
        } else {
            resultHandler.setValue(field, instance, value)
        }
    }

    fun <T> getValue(field: Field, entity: T): Any? {
        makeAccessible(field, entity)
        return field.get(entity)
    }

    fun <T> getValue(field: Field, entity: T, sqlType: SqlType): Any? {
        if (sqlType == SqlType.SELECT) {
            return getValue(field, entity)
        }
        makeAccessible(field, entity)
        val fillAnnotationHandlers = KiteConfig.fillHandlers
        val annotations = field.annotations
        for (annotation in annotations) {
            val handler = fillAnnotationHandlers[FillKey(annotation.annotationClass, sqlType)]
            if (handler != null) {
                return handler.fillValue(annotation, field, entity as Any)
            }
        }
        return getValue(field, entity)
    }

    /**
     * Get the value of a field from an object or a map.
     *
     * @param any The object or map from which to retrieve the value.
     * @param param The field name or a dot-separated path to the field.
     * @return The value of the field, or null if not found.
     */
    fun getValue(any: Any, param: String): Any? {
        val parts = param.split(DOT)
        var current: Any? = any
        for (part in parts) {
            current = when (current) {
                is Map<*, *> -> {
                    if (current.containsKey(part)) {
                        current[part]
                    } else {
                        throw NoSuchFieldException("Key not found: $part in map")
                    }
                }
                else -> {
                    val field = generateSequence(current?.javaClass) { it.superclass }
                        .flatMap { it.declaredFields.asSequence() }
                        .find { it.name == part }
                        ?: throw NoSuchFieldException("Field not found: $part in ${current?.javaClass}")
                    getValue(field, current)
                }
            }
            if (current == null) break
        }
        return current
    }

}
