package com.tang.kite.result.time

import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.result.ResultHandler
import com.tang.kite.utils.Reflects
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
        val localDate = when (value) {
            is Date -> value.toLocalDate()
            is Timestamp -> value.toLocalDateTime().toLocalDate()
            is LocalDate -> value
            is LocalDateTime -> value.toLocalDate()
            else -> throw UnsupportedTypeException(value::class, field)
        }
        Reflects.setValue(field, instance, localDate)
    }

}
