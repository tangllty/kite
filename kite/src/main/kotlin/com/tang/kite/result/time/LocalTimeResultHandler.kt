package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
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
        when (value) {
            is Date -> field.set(instance, value.toLocalDate().atStartOfDay().toLocalTime())
            is Time -> field.set(instance, value.toLocalTime())
            is Timestamp -> field.set(instance, value.toLocalDateTime().toLocalTime())
            is LocalTime -> field.set(instance, value)
            is LocalDateTime -> field.set(instance, value.toLocalTime())
            is Long -> field.set(instance, LocalTime.ofNanoOfDay(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
