package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * @author Tang
 */
class InstantResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, value.toInstant())
            is Time -> field.set(instance, value.toInstant())
            is Timestamp -> field.set(instance, value.toInstant())
            is LocalDate -> field.set(instance, value.atStartOfDay(ZoneId.systemDefault()).toInstant())
            is LocalTime -> {
                val dateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), value)
                field.set(instance, dateTime.atZone(ZoneId.systemDefault()).toInstant())
            }
            is LocalDateTime -> field.set(instance, value.atZone(ZoneId.systemDefault()).toInstant())
            is Long -> field.set(instance, Instant.ofEpochMilli(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
