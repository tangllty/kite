package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
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
        val instant = when (value) {
            is Date, is Time, is Timestamp -> Instant.ofEpochMilli(value.time)
            is LocalDate -> value.atStartOfDay(ZoneId.systemDefault()).toInstant()
            is LocalTime -> {
                val dateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), value)
                dateTime.atZone(ZoneId.systemDefault()).toInstant()
            }
            is LocalDateTime -> value.atZone(ZoneId.systemDefault()).toInstant()
            is Long -> Instant.ofEpochMilli(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, instant)
    }

}
