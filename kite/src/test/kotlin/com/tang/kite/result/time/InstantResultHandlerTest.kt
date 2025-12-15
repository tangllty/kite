package com.tang.kite.result.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class InstantResultHandlerTest {

    data class InstantData(
        var nullField: Instant? = null,
        var sqlDateField: Instant? = null,
        var timeField: Instant? = null,
        var timestampField: Instant? = null,
        var localDateField: Instant? = null,
        var localTimeField: Instant? = null,
        var localDateTimeField: Instant? = null,
        var longField: Instant? = null
    )

    @Test
    fun setValue() {
        val handler = InstantResultHandler()
        val instance = InstantData()
        val fields = InstantData::class.java.declaredFields
        val sqlDate = Date.valueOf("2024-06-01")
        val time = Time.valueOf("12:00:00")
        val timestamp = Timestamp.valueOf("2024-06-01 12:00:00")
        val localDate = LocalDate.of(2024, 6, 1)
        val localTime = LocalTime.of(12, 0)
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 0)
        val longValue = 1717214400000L // 2024-06-01 16:00:00 GMT

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance)
                "sqlDateField" -> handler.setValue(field, instance, sqlDate)
                "timeField" -> handler.setValue(field, instance, time)
                "timestampField" -> handler.setValue(field, instance, timestamp)
                "localDateField" -> handler.setValue(field, instance, localDate)
                "localTimeField" -> handler.setValue(field, instance, localTime)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
                "longField" -> handler.setValue(field, instance, longValue)
            }
        }

        assertNull(instance.nullField)

        val expectedSqlDateInstant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val expectedTimeInstant = LocalDateTime.of(LocalDate.ofEpochDay(0), localTime).atZone(ZoneId.systemDefault()).toInstant()
        val expectedTimestampInstant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val expectedLongInstant = Instant.ofEpochMilli(longValue)

        assertEquals(expectedSqlDateInstant, instance.sqlDateField)
        assertEquals(expectedTimeInstant, instance.timeField)
        assertEquals(expectedTimestampInstant, instance.timestampField)
        assertEquals(expectedSqlDateInstant, instance.localDateField)
        assertEquals(expectedTimeInstant, instance.localTimeField)
        assertEquals(expectedTimestampInstant, instance.localDateTimeField)
        assertEquals(expectedLongInstant, instance.longField)
    }

}
