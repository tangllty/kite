package com.tang.kite.result.primitive

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
class ByteResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val byte = when (value) {
            is Byte -> value
            is Number -> value.toByte()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, byte)
    }

}
