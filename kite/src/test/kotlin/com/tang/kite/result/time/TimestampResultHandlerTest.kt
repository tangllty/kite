package com.tang.kite.result.time

import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * author Tang
 */
class TimestampResultHandlerTest {

    data class TimestampData(
        var nullField: Timestamp? = null,
        var timestampField: Timestamp? = null,
        var dateField: Timestamp? = null,
        var localDateTimeField: Timestamp? = null,
        var longField: Timestamp? = null,
    )

    @Test
    fun setValue() {
        val handler = TimestampResultHandler()
        val instance = TimestampData()
        val fields = TimestampData::class.java.declaredFields
        val expectedTimestamp = Timestamp.valueOf("2024-06-01 12:30:45")
        val date = Date(expectedTimestamp.time)
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 30, 45)
        val longValue = expectedTimestamp.time

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance)
                "timestampField" -> handler.setValue(field, instance, expectedTimestamp)
                "dateField" -> handler.setValue(field, instance, date)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
                "longField" -> handler.setValue(field, instance, longValue)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedTimestamp, instance.timestampField)
        assertEquals(Timestamp(date.time), instance.dateField)
        assertEquals(Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()), instance.localDateTimeField)
        assertEquals(Timestamp(longValue), instance.longField)
    }

}
