package com.tang.kite.result.time

import com.tang.kite.result.ResultHandler
import java.lang.reflect.Field
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Tang
 */
class LocalDateTimeResultHandler : ResultHandler {

    override fun <T> setValue(field: Field, instance: T, value: Any) {
        when (value) {
            is Date -> field.set(instance, value.toLocalDate().atStartOfDay())
            is Timestamp -> field.set(instance, value.toLocalDateTime())
            is LocalDate -> field.set(instance, value.atStartOfDay())
            is LocalDateTime -> field.set(instance, value)
            is Long -> field.set(instance, LocalDateTime.ofEpochSecond(value / 1000, (value % 1000 * 1_000_000).toInt(), ZoneOffset.UTC))
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java.name} for field: ${field.name}")
        }
    }

}
