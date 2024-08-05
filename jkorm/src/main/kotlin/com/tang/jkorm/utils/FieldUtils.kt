package com.tang.jkorm.utils

import com.tang.jkorm.function.SFunction
import java.lang.invoke.SerializedLambda
import java.lang.reflect.Field
import java.util.Locale
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
object FieldUtils {

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

}
