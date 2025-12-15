package com.tang.kite.result.time

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class SqlDateResultHandlerTest {

    data class SqlDateData(
        var nullField: Date? = null,
        var sqlDateField: Date? = null,
        var timeField: Date? = null,
        var timestampField: Date? = null,
        var localDateField: Date? = null,
        var localTimeField: Date? = null,
        var localDateTimeField: Date? = null,
        var longField: Date? = null
    )

    @Test
    fun setValue() {
        val handler = SqlDateResultHandler()
        val instance = SqlDateData()
        val fields = SqlDateData::class.java.declaredFields
        val expectedDate = Date.valueOf("2024-06-01")
        val time = Time.valueOf("12:00:00")
        val timestamp = Timestamp.valueOf("2024-06-01 12:00:00")
        val localDate = LocalDate.of(2024, 6, 1)
        val localTime = LocalTime.of(12, 0)
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 0)
        val longValue = expectedDate.time

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance)
                "sqlDateField" -> handler.setValue(field, instance, expectedDate)
                "timeField" -> handler.setValue(field, instance, time)
                "timestampField" -> handler.setValue(field, instance, timestamp)
                "localDateField" -> handler.setValue(field, instance, localDate)
                "localTimeField" -> handler.setValue(field, instance, localTime)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
                "longField" -> handler.setValue(field, instance, longValue)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedDate, instance.sqlDateField)
        assertEquals(Date(time.time), instance.timeField)
        assertEquals(Date(timestamp.time), instance.timestampField)
        assertEquals(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), instance.localDateField)
        assertEquals(Date.from(LocalDateTime.of(LocalDate.ofEpochDay(0), localTime).atZone(ZoneId.systemDefault()).toInstant()), instance.localTimeField)
        assertEquals(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()), instance.localDateTimeField)
        assertEquals(Date(longValue), instance.longField)
    }

}
