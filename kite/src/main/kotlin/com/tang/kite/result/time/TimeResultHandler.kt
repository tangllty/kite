package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author Tang
 */
class TimeResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Time -> field.set(instance, value.toLocalTime())
            is LocalTime -> field.set(instance, value)
            is LocalDateTime -> field.set(instance, value.toLocalTime())
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
