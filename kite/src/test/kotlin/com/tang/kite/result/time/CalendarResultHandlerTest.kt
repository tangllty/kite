package com.tang.kite.result.time

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * author Tang
 */
class CalendarResultHandlerTest {

    data class CalendarData(
        var nullField: Calendar? = null,
        var dateField: Calendar? = null,
        var timeField: Calendar? = null,
        var timestampField: Calendar? = null,
        var localDateField: Calendar? = null,
        var localTimeField: Calendar? = null,
        var localDateTimeField: Calendar? = null,
        var longField: Calendar? = null,
    )

    @Test
    fun setValue() {
        val handler = CalendarResultHandler()
        val instance = CalendarData()
        val fields = CalendarData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "dateField" -> {
                    val date = Date.valueOf("2024-06-01")
                    handler.setValue(field, instance, date)
                    assertNotNull(instance.dateField)
                    assertEquals(2024, instance.dateField?.get(Calendar.YEAR))
                    assertEquals(5, instance.dateField?.get(Calendar.MONTH))
                    assertEquals(1, instance.dateField?.get(Calendar.DAY_OF_MONTH))
                }
                "timeField" -> {
                    val time = Time.valueOf("12:30:00")
                    handler.setValue(field, instance, time)
                    assertNotNull(instance.timeField)
                    assertEquals(12, instance.timeField?.get(Calendar.HOUR_OF_DAY))
                    assertEquals(30, instance.timeField?.get(Calendar.MINUTE))
                    assertEquals(0, instance.timeField?.get(Calendar.SECOND))
                }
                "timestampField" -> {
                    val ts = Timestamp.valueOf("2024-06-01 12:30:00")
                    handler.setValue(field, instance, ts)
                    assertNotNull(instance.timestampField)
                    assertEquals(2024, instance.timestampField?.get(Calendar.YEAR))
                    assertEquals(5, instance.timestampField?.get(Calendar.MONTH))
                    assertEquals(1, instance.timestampField?.get(Calendar.DAY_OF_MONTH))
                    assertEquals(12, instance.timestampField?.get(Calendar.HOUR_OF_DAY))
                    assertEquals(30, instance.timestampField?.get(Calendar.MINUTE))
                    assertEquals(0, instance.timestampField?.get(Calendar.SECOND))
                }
                "localDateField" -> {
                    val localDate = LocalDate.of(2024, 6, 1)
                    handler.setValue(field, instance, localDate)
                    assertNotNull(instance.localDateField)
                    assertEquals(2024, instance.localDateField?.get(Calendar.YEAR))
                    assertEquals(5, instance.localDateField?.get(Calendar.MONTH))
                    assertEquals(1, instance.localDateField?.get(Calendar.DAY_OF_MONTH))
                }
                "localTimeField" -> {
                    val localTime = LocalTime.of(12, 30, 0)
                    handler.setValue(field, instance, localTime)
                    assertNotNull(instance.localTimeField)
                    assertEquals(12, instance.localTimeField?.get(Calendar.HOUR_OF_DAY))
                    assertEquals(30, instance.localTimeField?.get(Calendar.MINUTE))
                    assertEquals(0, instance.localTimeField?.get(Calendar.SECOND))
                }
                "localDateTimeField" -> {
                    val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 30, 0)
                    handler.setValue(field, instance, localDateTime)
                    assertNotNull(instance.localDateTimeField)
                    assertEquals(2024, instance.localDateTimeField?.get(Calendar.YEAR))
                    assertEquals(5, instance.localDateTimeField?.get(Calendar.MONTH))
                    assertEquals(1, instance.localDateTimeField?.get(Calendar.DAY_OF_MONTH))
                    assertEquals(12, instance.localDateTimeField?.get(Calendar.HOUR_OF_DAY))
                    assertEquals(30, instance.localDateTimeField?.get(Calendar.MINUTE))
                    assertEquals(0, instance.localDateTimeField?.get(Calendar.SECOND))
                }
                "longField" -> {
                    val millis = Date.valueOf("2024-06-01").time
                    handler.setValue(field, instance, millis)
                    assertNotNull(instance.longField)
                    assertEquals(2024, instance.longField?.get(Calendar.YEAR))
                    assertEquals(5, instance.longField?.get(Calendar.MONTH))
                    assertEquals(1, instance.longField?.get(Calendar.DAY_OF_MONTH))
                }
            }
        }
        assertNull(instance.nullField)
    }

}
