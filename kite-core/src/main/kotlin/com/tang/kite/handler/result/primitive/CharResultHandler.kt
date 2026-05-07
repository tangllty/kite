package com.tang.kite.handler.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.handler.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
object CharResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val char = when (value) {
            is Char -> value
            is String -> {
                if (value.length == 1) {
                    value[0]
                } else {
                    throw IllegalArgumentException("String value must be a single character for field: ${field.name}")
                }
            }
            is Number -> value.toInt().toChar()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, char)
    }

}
