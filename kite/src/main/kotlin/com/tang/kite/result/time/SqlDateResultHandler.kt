package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * @author Tang
 */
class SqlDateResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, value)
            is Time -> field.set(instance, Date(value.time))
            is Timestamp -> field.set(instance, Date(value.time))
            is LocalDate -> field.set(instance, Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            is LocalTime -> {
                val dateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), value)
                val date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
                field.set(instance, date)
            }
            is LocalDateTime -> field.set(instance, Date.from(value.atZone(ZoneId.systemDefault()).toInstant()))
            is Long -> field.set(instance, Date(value))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
