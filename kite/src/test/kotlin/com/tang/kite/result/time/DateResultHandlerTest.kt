package com.tang.kite.result.time

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.sql.Date as SqlDate
import java.util.Date

/**
 * author Tang
 */
class DateResultHandlerTest {

    data class DateData(
        var nullField: Date? = null,
        var dateField: Date? = null,
        var timeField: Date? = null,
        var timestampField: Date? = null,
        var localDateField: Date? = null,
        var localTimeField: Date? = null,
        var localDateTimeField: Date? = null,
        var longField: Date? = null,
    )

    @Test
    fun setValue() {
        val handler = DateResultHandler()
        val instance = DateData()
        val fields = DateData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "dateField" -> {
                    val date = SqlDate(1717200000000L) // 2024-06-01 12:00:00 GMT
                    handler.setValue(field, instance, date)
                }
                "timeField" -> {
                    val time = Time(1717203600000L) // 2024-06-01 13:00:00 GMT
                    handler.setValue(field, instance, time)
                }
                "timestampField" -> {
                    val timestamp = Timestamp(1717207200000L) // 2024-06-01 14:00:00 GMT
                    handler.setValue(field, instance, timestamp)
                }
                "localDateField" -> {
                    val localDate = LocalDate.of(2024, 6, 1)
                    handler.setValue(field, instance, localDate)
                }
                "localTimeField" -> {
                    val localTime = LocalTime.of(15, 0)
                    handler.setValue(field, instance, localTime)
                }
                "localDateTimeField" -> {
                    val localDateTime = LocalDateTime.of(2024, 6, 1, 16, 0)
                    handler.setValue(field, instance, localDateTime)
                }
                "longField" -> {
                    val longValue = 1717214400000L // 2024-06-01 16:00:00 GMT
                    handler.setValue(field, instance, longValue)
                }
            }
        }
        assertNull(instance.nullField)
        assertNotNull(instance.dateField)
        assertEquals(1717200000000L, instance.dateField?.time)
        assertNotNull(instance.timeField)
        assertEquals(1717203600000L, instance.timeField?.time)
        assertNotNull(instance.timestampField)
        assertEquals(1717207200000L, instance.timestampField?.time)
        assertNotNull(instance.localDateField)
        assertEquals(LocalDate.of(2024, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(), instance.localDateField?.time)
        assertNotNull(instance.localTimeField)
        assertEquals(LocalDateTime.of(LocalDate.ofEpochDay(0), LocalTime.of(15, 0)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), instance.localTimeField?.time)
        assertNotNull(instance.localDateTimeField)
        assertEquals(LocalDateTime.of(2024, 6, 1, 16, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), instance.localDateTimeField?.time)
        assertNotNull(instance.longField)
        assertEquals(1717214400000L, instance.longField?.time)
    }

}
