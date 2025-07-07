package com.tang.kite.utils

import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.function.SFunction
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
        Reflects.makeAccessible(writeReplaceMethod, function)
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
        Reflects.makeAccessible(field, instance)
        if (value == null) {
            field.set(instance, null)
            return
        }
        when (field.type) {
            Char::class.java -> field.set(instance, (value as String).first())
            Short::class.java -> field.set(instance, (value as Number).toShort())
            Byte::class.java -> field.set(instance, (value as Number).toByte())
            Int::class.java, Int::class.javaObjectType, Integer::class.java -> field.set(instance, (value as Number).toInt())
            Long::class.java, Long::class.javaObjectType -> field.set(instance, (value as Number).toLong())
            Double::class.java -> field.set(instance, (value as Number).toDouble())
            Float::class.java -> field.set(instance, (value as Number).toFloat())
            Boolean::class.java -> field.set(instance, value.toString().toBoolean())
            String::class.java -> field.set(instance, value.toString())
            else -> {
                if (field.type.isEnum) {
                    val enumValue = value.toString()
                    @Suppress("UNCHECKED_CAST")
                    val enumClass = field.type as Class<Enum<*>>
                    val enumConstant = enumClass.enumConstants.firstOrNull { it.name == enumValue }
                    if (enumConstant == null) {
                        throw IllegalArgumentException("Invalid enum value: $enumValue for field: ${field.name}")
                    }
                    field.set(instance, enumConstant)
                } else {
                    field.set(instance, value)
                }
            }
        }
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
                    field.isAccessible = true
                    field.get(current)
                }
            }
            if (current == null) break
        }
        return current
    }

}
