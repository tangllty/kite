package com.tang.kite.result.math

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.math.BigDecimal

/**
 * @author Tang
 */
class BigDecimalResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is BigDecimal -> field.set(instance, value)
            is String -> field.set(instance, BigDecimal(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
