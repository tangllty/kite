package com.tang.kite.result.time

import java.sql.Time
import java.time.LocalTime
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * author Tang
 */
class TimeResultHandlerTest {

    data class TimeData(
        var nullField: Time? = null,
        var timeField: Time? = null,
        var localTimeField: Time? = null,
        var localDateTimeField: Time? = null
    )

    @Test
    fun setValue() {
        val handler = TimeResultHandler()
        val instance = TimeData()
        val fields = TimeData::class.java.declaredFields
        val expectedTime = Time.valueOf("12:30:00")
        val localTime = LocalTime.of(12, 30)
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 30)

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance)
                "timeField" -> handler.setValue(field, instance, expectedTime)
                "localTimeField" -> handler.setValue(field, instance, localTime)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedTime, instance.timeField)
        assertEquals(Time.valueOf(localTime), instance.localTimeField)
        assertEquals(Time.valueOf(localDateTime.toLocalTime()), instance.localDateTimeField)
    }

}
