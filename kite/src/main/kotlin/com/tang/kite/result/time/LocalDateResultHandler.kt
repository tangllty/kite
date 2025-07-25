package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author Tang
 */
class LocalDateResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, value.toLocalDate())
            is Timestamp -> field.set(instance, value.toLocalDateTime().toLocalDate())
            is LocalDate -> field.set(instance, value)
            is LocalDateTime -> field.set(instance, value.toLocalDate())
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
