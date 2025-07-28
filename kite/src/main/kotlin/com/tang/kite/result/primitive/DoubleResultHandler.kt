package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class DoubleResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val double = when (value) {
            is Double -> value
            is Number -> value.toDouble()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, double)
    }

}
