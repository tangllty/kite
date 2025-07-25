package com.tang.kite.result.primitive

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field

/**
 * @author Tang
 */
class BooleanResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Boolean -> field.set(instance, value)
            is Number -> field.set(instance, value.toInt() != 0)
            is String -> {
                val lowerValue = value.lowercase()
                when (lowerValue) {
                    "true", "1" -> field.set(instance, true)
                    "false", "0" -> field.set(instance, false)
                    else -> throw IllegalArgumentException("Unsupported string value: $value for field: ${field.name}")
                }
            }
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
