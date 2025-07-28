package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class FloatResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val float = when (value) {
            is Float -> value
            is Number -> value.toFloat()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, float)
    }

}
