package com.tang.kite.result.primitive

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class IntResultHandlerTest {

    data class IntData(
        var nullField: Int? = null,
        var intField: Int? = null,
        var longField: Int? = null,
        var shortField: Int? = null,
        var floatField: Int? = null,
        var doubleField: Int? = null,
        var byteField: Int? = null,
    )

    @Test
    fun setValue() {
        val handler = IntResultHandler()
        val instance = IntData()
        val fields = IntData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "intField" -> handler.setValue(field, instance, 123)
                "longField" -> handler.setValue(field, instance, 456L)
                "shortField" -> handler.setValue(field, instance, 78.toShort())
                "floatField" -> handler.setValue(field, instance, 9.01f)
                "doubleField" -> handler.setValue(field, instance, 23.45)
                "byteField" -> handler.setValue(field, instance, 6.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals(123, instance.intField)
        assertEquals(456, instance.longField)
        assertEquals(78, instance.shortField)
        assertEquals(9.01f.toInt(), instance.floatField)
        assertEquals(23.45.toInt(), instance.doubleField)
        assertEquals(6, instance.byteField)
    }

}
