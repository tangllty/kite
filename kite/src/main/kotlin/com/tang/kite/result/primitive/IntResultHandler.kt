package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class IntResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val int = when (value) {
            is Int -> value
            is Number -> value.toInt()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, int)
    }

}
