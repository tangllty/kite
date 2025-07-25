package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author Tang
 */
class CalendarResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, value)
            is Time -> field.set(instance, value)
            is Timestamp -> field.set(instance, value)
            is LocalDate -> field.set(instance, Date.valueOf(value))
            is LocalTime -> field.set(instance, Time.valueOf(value))
            is LocalDateTime -> field.set(instance, Timestamp.valueOf(value))
            is Long -> field.set(instance, Date(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
