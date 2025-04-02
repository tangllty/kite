package com.tang.kite.utils

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

    fun setValue(field: Field, instance: Any, value: Any?) {
        field.isAccessible = true
        if (value == null) {
            field.set(instance, null)
            return
        }
        when (field.type) {
            Char::class.java -> field.set(instance, (value as String).first())
            Short::class.java -> field.set(instance, (value as Number).toShort())
            Byte::class.java -> field.set(instance, (value as Number).toByte())
            Int::class.java, Integer::class.java -> field.set(instance, (value as Number).toInt())
            Long::class.java -> field.set(instance, (value as Number).toLong())
            Double::class.java -> field.set(instance, (value as Number).toDouble())
            Float::class.java -> field.set(instance, (value as Number).toFloat())
            Boolean::class.java -> field.set(instance, value.toString().toBoolean())
            String::class.java -> field.set(instance, value.toString())
            else -> if (field.type.isEnum) {
                val enumValue = value.toString()
                @Suppress("UNCHECKED_CAST")
                val enumClass = field.type as Class<Enum<*>>
                val enumConstant = enumClass.enumConstants.firstOrNull { it.name == enumValue }
                if (enumConstant == null) {
                    throw IllegalArgumentException("Invalid enum value: $enumValue for field: $field.name")
                }
                field.set(instance, enumConstant)
            }
        }
    }

}
