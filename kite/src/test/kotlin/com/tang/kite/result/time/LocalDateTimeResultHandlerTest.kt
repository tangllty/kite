package com.tang.kite.result.time

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.sql.Date
import java.sql.Timestamp
import java.time.ZoneOffset

/**
 * author Tang
 */
class LocalDateTimeResultHandlerTest {

    data class LocalDateTimeData(
        var nullField: LocalDateTime? = null,
        var localDateTimeField: LocalDateTime? = null,
        var sqlDateField: LocalDateTime? = null,
        var timestampField: LocalDateTime? = null,
        var localDateField: LocalDateTime? = null,
        var longField: LocalDateTime? = null
    )

    @Test
    fun setValue() {
        val handler = LocalDateTimeResultHandler()
        val instance = LocalDateTimeData()
        val fields = LocalDateTimeData::class.java.declaredFields
        val expectedDateTime = LocalDateTime.of(2024, 6, 1, 12, 30, 0)
        val sqlDate = Date.valueOf("2024-06-01")
        val timestamp = Timestamp.valueOf(expectedDateTime)
        val localDate = LocalDate.of(2024, 6, 1)
        val longValue = expectedDateTime.toEpochSecond(ZoneOffset.UTC) * 1000

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "sqlDateField" -> handler.setValue(field, instance, sqlDate)
                "timestampField" -> handler.setValue(field, instance, timestamp)
                "localDateField" -> handler.setValue(field, instance, localDate)
                "localDateTimeField" -> handler.setValue(field, instance, expectedDateTime)
                "longField" -> handler.setValue(field, instance, longValue)
            }
        }

        assertNull(instance.nullField)
        assertEquals(sqlDate.toLocalDate().atStartOfDay(), instance.sqlDateField)
        assertEquals(timestamp.toLocalDateTime(), instance.timestampField)
        assertEquals(localDate.atStartOfDay(), instance.localDateField)
        assertEquals(expectedDateTime, instance.localDateTimeField)
        assertEquals(LocalDateTime.ofEpochSecond(longValue / 1000, (longValue % 1000 * 1000000).toInt(), ZoneOffset.UTC), instance.longField)
    }

}
