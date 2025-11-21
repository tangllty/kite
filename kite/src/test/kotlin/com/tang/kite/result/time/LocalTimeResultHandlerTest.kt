package com.tang.kite.result.time

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalTimeResultHandlerTest {

    data class LocalTimeData(
        var nullField: LocalTime? = null,
        var localTimeField: LocalTime? = null,
        var sqlDateField: LocalTime? = null,
        var timeField: LocalTime? = null,
        var timestampField: LocalTime? = null,
        var localDateTimeField: LocalTime? = null,
        var longField: LocalTime? = null
    )

    @Test
    fun setValue() {
        val handler = LocalTimeResultHandler()
        val instance = LocalTimeData()
        val fields = LocalTimeData::class.java.declaredFields
        val expectedTime = LocalTime.of(12, 30, 0)
        val sqlDate = Date.valueOf("2024-06-01")
        val time = Time.valueOf(expectedTime)
        val timestamp = Timestamp.valueOf(LocalDateTime.of(2024, 6, 1, 12, 30))
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 30)
        val longValue = expectedTime.toNanoOfDay()

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "localTimeField" -> handler.setValue(field, instance, expectedTime)
                "sqlDateField" -> handler.setValue(field, instance, sqlDate)
                "timeField" -> handler.setValue(field, instance, time)
                "timestampField" -> handler.setValue(field, instance, timestamp)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
                "longField" -> handler.setValue(field, instance, longValue)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedTime, instance.localTimeField)
        assertEquals(sqlDate.toLocalDate().atStartOfDay().toLocalTime(), instance.sqlDateField)
        assertEquals(time.toLocalTime(), instance.timeField)
        assertEquals(timestamp.toLocalDateTime().toLocalTime(), instance.timestampField)
        assertEquals(localDateTime.toLocalTime(), instance.localDateTimeField)
        assertEquals(LocalTime.ofNanoOfDay(longValue), instance.longField)
    }

}
