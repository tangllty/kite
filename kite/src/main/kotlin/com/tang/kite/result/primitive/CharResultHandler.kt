package com.tang.kite.result.primitive

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field

/**
 * @author Tang
 */
class CharResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Char -> field.set(instance, value)
            is String -> {
                if (value.length == 1) {
                    field.set(instance, value[0])
                } else {
                    throw IllegalArgumentException("String value must be a single character for field: ${field.name}")
                }
            }
            is Number -> field.set(instance, value.toInt().toChar())
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
