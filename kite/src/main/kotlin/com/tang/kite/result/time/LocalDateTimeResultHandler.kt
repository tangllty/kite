package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
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
        val localDateTime = when (value) {
            is Date -> value.toLocalDate().atStartOfDay()
            is Timestamp -> value.toLocalDateTime()
            is LocalDate -> value.atStartOfDay()
            is LocalDateTime -> value
            is Long -> LocalDateTime.ofEpochSecond(value / 1000, (value % 1000 * 1_000_000).toInt(), ZoneOffset.UTC)
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, localDateTime)
    }

}
