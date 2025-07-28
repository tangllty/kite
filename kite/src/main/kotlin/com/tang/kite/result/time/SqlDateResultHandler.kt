package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
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
        val sqlDate = when (value) {
            is Date -> value
            is Time, is Timestamp -> Date(value.time)
            is LocalDate -> Date.valueOf(value)
            is LocalTime -> {
                val dateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), value)
                val date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
                Date(date.time)
            }
            is LocalDateTime -> {
                val date = Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
                Date(date.time)
            }
            is Long -> Date(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, sqlDate)
    }

}
