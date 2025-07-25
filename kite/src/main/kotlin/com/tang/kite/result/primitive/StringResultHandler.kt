package com.tang.kite.result.primitive

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Clob

/**
 * @author Tang
 */
class StringResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is String -> field.set(instance, value)
            is Number, is Boolean, is Char -> field.set(instance, value.toString())
            is Clob -> field.set(instance, value.getSubString(1, value.length().toInt()))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
