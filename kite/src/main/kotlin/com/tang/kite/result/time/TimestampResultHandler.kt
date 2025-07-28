package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author Tang
 */
class TimestampResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, Timestamp(value.time))
            is Timestamp -> field.set(instance, value)
            is LocalDate -> field.set(instance, Timestamp.valueOf(value.atStartOfDay()))
            is LocalDateTime -> field.set(instance, Timestamp.valueOf(value))
            is Number -> field.set(instance, Timestamp(value.toLong()))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
