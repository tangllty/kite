package com.tang.kite.result.primitive

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class DoubleResultHandlerTest {

    data class DoubleData(
        var nullField: Double? = null,
        var doubleField: Double? = null,
        var floatField: Double? = null,
        var intField: Double? = null,
        var longField: Double? = null,
        var shortField: Double? = null,
        var byteField: Double? = null,
    )

    @Test
    fun setValue() {
        val handler = DoubleResultHandler()
        val instance = DoubleData()
        val fields = DoubleData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "doubleField" -> handler.setValue(field, instance, 12.34)
                "floatField" -> handler.setValue(field, instance, 56.78f)
                "intField" -> handler.setValue(field, instance, 123)
                "longField" -> handler.setValue(field, instance, 456L)
                "shortField" -> handler.setValue(field, instance, 78.toShort())
                "byteField" -> handler.setValue(field, instance, 9.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals(12.34, instance.doubleField)
        assertEquals(56.78f.toDouble(), instance.floatField)
        assertEquals(123.0, instance.intField)
        assertEquals(456.0, instance.longField)
        assertEquals(78.0, instance.shortField)
        assertEquals(9.0, instance.byteField)
    }

}
