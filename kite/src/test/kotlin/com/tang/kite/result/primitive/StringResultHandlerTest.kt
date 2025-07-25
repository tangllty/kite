package com.tang.kite.result.primitive

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

/**
 * @author Tang
 */
class StringResultHandlerTest {

    data class StringData(
        var nullField: String? = null,
        var stringField: String? = null,
        var intField: String? = null,
        var booleanField: String? = null,
        var doubleField: String? = null,
        var floatField: String? = null,
        var longField: String? = null,
        var shortField: String? = null,
    )

    @Test
    fun setValue() {
        val handler = StringResultHandler()
        val instance = StringData()
        val fields = StringData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "stringField" -> handler.setValue(field, instance, "test")
                "intField" -> handler.setValue(field, instance, 123)
                "booleanField" -> handler.setValue(field, instance, true)
                "doubleField" -> handler.setValue(field, instance, 45.67)
                "floatField" -> handler.setValue(field, instance, 89.01f)
                "longField" -> handler.setValue(field, instance, 123456789L)
                "shortField" -> handler.setValue(field, instance, 12.toShort())
            }
        }
        assertNull(instance.nullField)
        assertEquals("test", instance.stringField)
        assertEquals("123", instance.intField)
        assertEquals("true", instance.booleanField)
        assertEquals("45.67", instance.doubleField)
        assertEquals("89.01", instance.floatField)
        assertEquals("123456789", instance.longField)
        assertEquals("12", instance.shortField)
    }

}
