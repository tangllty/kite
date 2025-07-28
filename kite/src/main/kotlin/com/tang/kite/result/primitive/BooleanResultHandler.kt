package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class BooleanResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val boolean = when (value) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> {
                val lowerValue = value.lowercase()
                when (lowerValue) {
                    "true", "1" -> true
                    "false", "0" -> false
                    else -> throw IllegalArgumentException("Unsupported string value: $value for field: ${field.name}")
                }
            }
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, boolean)
    }

}
