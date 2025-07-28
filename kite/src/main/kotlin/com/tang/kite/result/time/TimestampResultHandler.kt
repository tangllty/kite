package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
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
        val timestamp = when (value) {
            is Date -> Timestamp(value.time)
            is Timestamp -> value
            is LocalDate -> Timestamp.valueOf(value.atStartOfDay())
            is LocalDateTime -> Timestamp.valueOf(value)
            is Number -> Timestamp(value.toLong())
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, timestamp)
    }

}
