package com.tang.kite.result.math

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.math.BigDecimal

/**
 * @author Tang
 */
class BigDecimalResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val bigDecimal = when (value) {
            is BigDecimal -> value
            is String -> BigDecimal(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, bigDecimal)
    }

}
