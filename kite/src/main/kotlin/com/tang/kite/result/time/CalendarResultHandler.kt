package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
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
        when (value) {
            is Date -> {
                val calendar = Calendar.getInstance()
                calendar.time = value
                field.set(instance, calendar)
            }
            is Time -> {
                val calendar = Calendar.getInstance()
                calendar.time = value
                field.set(instance, calendar)
            }
            is Timestamp -> {
                val calendar = Calendar.getInstance()
                calendar.time = value
                field.set(instance, calendar)
            }
            is LocalDate -> {
                val calendar = Calendar.getInstance()
                calendar.set(value.year, value.monthValue - 1, value.dayOfMonth)
                field.set(instance, calendar)
            }
            is LocalTime -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, value.hour)
                calendar.set(Calendar.MINUTE, value.minute)
                calendar.set(Calendar.SECOND, value.second)
                field.set(instance, calendar)
            }
            is LocalDateTime -> {
                val calendar = Calendar.getInstance()
                calendar.set(value.year, value.monthValue - 1, value.dayOfMonth, value.hour, value.minute, value.second)
                field.set(instance, calendar)
            }
            is Long -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = value
                field.set(instance, calendar)
            }
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
