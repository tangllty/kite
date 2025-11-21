package com.tang.kite.result.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.sql.Date
import java.sql.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * author Tang
 */
class LocalDateResultHandlerTest {

    data class LocalDateData(
        var nullField: LocalDate? = null,
        var localDateField: LocalDate? = null,
        var sqlDateField: LocalDate? = null,
        var timestampField: LocalDate? = null,
        var localDateTimeField: LocalDate? = null
    )

    @Test
    fun setValue() {
        val handler = LocalDateResultHandler()
        val instance = LocalDateData()
        val fields = LocalDateData::class.java.declaredFields
        val sqlDate = Date.valueOf("2024-06-01")
        val timestamp = Timestamp.valueOf("2024-06-01 12:30:00")
        val expectedDate = LocalDate.of(2024, 6, 1)
        val localDateTime = LocalDateTime.of(2024, 6, 1, 12, 30)

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "sqlDateField" -> handler.setValue(field, instance, sqlDate)
                "timestampField" -> handler.setValue(field, instance, timestamp)
                "localDateField" -> handler.setValue(field, instance, expectedDate)
                "localDateTimeField" -> handler.setValue(field, instance, localDateTime)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedDate, instance.sqlDateField)
        assertEquals(expectedDate, instance.timestampField)
        assertEquals(expectedDate, instance.localDateField)
        assertEquals(expectedDate, instance.localDateTimeField)
    }

}
