package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class ShortResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val short = when (value) {
            is Short -> value
            is Number -> value.toShort()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, short)
    }

}
