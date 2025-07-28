package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.sql.Clob

/**
 * @author Tang
 */
class StringResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val string = when (value) {
            is String -> value
            is Number, is Boolean, is Char -> value.toString()
            is Clob -> value.getSubString(1, value.length().toInt())
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, string)
    }

}
