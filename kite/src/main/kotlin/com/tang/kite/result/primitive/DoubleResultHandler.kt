package com.tang.kite.result.primitive

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field

/**
 * @author Tang
 */
class DoubleResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Double -> field.set(instance, value)
            is Number -> field.set(instance, value.toDouble())
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
