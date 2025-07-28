package com.tang.kite.result.math

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.math.BigInteger

/**
 * @author Tang
 */
class BigIntegerResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val bigInteger = when (value) {
            is BigInteger -> value
            is String -> BigInteger(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, bigInteger)
    }

}
