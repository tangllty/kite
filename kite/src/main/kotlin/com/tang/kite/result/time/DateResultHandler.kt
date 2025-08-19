package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

/**
 * @author Tang
 */
class DateResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val date = when (value) {
            is SqlDate -> value
            is Time, is Timestamp -> Date(value.time)
            is LocalDate -> Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant())
            is LocalTime -> {
                val dateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), value)
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
            }
            is LocalDateTime -> Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
            is Long -> Date(value)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, date)
    }

}
