package com.tang.kite.result.primitive

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class FloatResultHandlerTest {

    data class FloatData(
        var nullField: Float? = null,
        var floatField: Float? = null,
        var intField: Float? = null,
        var shortField: Float? = null,
        var longField: Float? = null,
        var doubleField: Float? = null,
        var byteField: Float? = null,
    )

    @Test
    fun setValue() {
        val handler = FloatResultHandler()
        val instance = FloatData()
        val fields = FloatData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "floatField" -> handler.setValue(field, instance, 12.34f)
                "intField" -> handler.setValue(field, instance, 56)
                "shortField" -> handler.setValue(field, instance, 78.toShort())
                "longField" -> handler.setValue(field, instance, 90L)
                "doubleField" -> handler.setValue(field, instance, 123.45)
                "byteField" -> handler.setValue(field, instance, 67.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals(12.34f, instance.floatField)
        assertEquals(56f, instance.intField)
        assertEquals(78f, instance.shortField)
        assertEquals(90f, instance.longField)
        assertEquals(123.45f, instance.doubleField)
        assertEquals(67f, instance.byteField)
    }

}
