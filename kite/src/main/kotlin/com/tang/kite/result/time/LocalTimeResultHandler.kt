package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author Tang
 */
class LocalTimeResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val localTime = when (value) {
            is Date -> value.toLocalDate().atStartOfDay().toLocalTime()
            is Time -> value.toLocalTime()
            is Timestamp -> value.toLocalDateTime().toLocalTime()
            is LocalTime -> value
            is LocalDateTime -> value.toLocalTime()
            is Long -> LocalTime.ofNanoOfDay(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, localTime)
    }

}
