package com.tang.kite.result.primitive

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field

/**
 * @author Tang
 */
class IntResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Int -> field.set(instance, value)
            is Number -> field.set(instance, value.toInt())
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
