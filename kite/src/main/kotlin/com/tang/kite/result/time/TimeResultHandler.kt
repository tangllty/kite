package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author Tang
 */
class TimeResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        val time = when (value) {
            is Time -> value
            is LocalTime -> Time.valueOf(value)
            is LocalDateTime -> Time.valueOf(value.toLocalTime())
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, time)
    }

}
