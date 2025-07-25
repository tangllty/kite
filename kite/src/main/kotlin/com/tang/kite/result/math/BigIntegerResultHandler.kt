package com.tang.kite.result.math

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.math.BigInteger

/**
 * @author Tang
 */
class BigIntegerResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is BigInteger -> field.set(instance, value)
            is String -> field.set(instance, BigInteger(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
