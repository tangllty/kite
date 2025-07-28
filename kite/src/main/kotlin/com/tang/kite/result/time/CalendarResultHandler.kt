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
import java.util.Calendar

/**
 * @author Tang
 */
class CalendarResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val calendar = getCalendarInstance {
            when (value) {
                is Date -> time = value
                is Time -> time = value
                is Timestamp -> time = value
                is LocalDate -> set(value.year, value.monthValue - 1, value.dayOfMonth)
                is LocalTime -> {
                    set(Calendar.HOUR_OF_DAY, value.hour)
                    set(Calendar.MINUTE, value.minute)
                    set(Calendar.SECOND, value.second)
                }
                is LocalDateTime -> set(value.year, value.monthValue - 1, value.dayOfMonth, value.hour, value.minute, value.second)
                is Long -> timeInMillis = value
                is Calendar -> timeInMillis = value.timeInMillis
                else -> throw UnsupportedTypeException(value::class, field)
            }
        }
        Reflects.setValue(field, instance, calendar)
    }

    private fun getCalendarInstance(block: Calendar.() -> Unit): Calendar {
        return Calendar.getInstance().apply(block)
    }

}
