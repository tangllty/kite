package com.tang.kite.result.primitive

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class ShortResultHandlerTest {

    data class ShortData(
        var nullField: Short? = null,
        var shortField: Short? = null,
        var intField: Short? = null,
        var longField: Short? = null,
        var floatField: Short? = null,
        var doubleField: Short? = null,
        var byteField: Short? = null,
    )

    @Test
    fun setValue() {
        val handler = ShortResultHandler()
        val instance = ShortData()
        val fields = ShortData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "shortField" -> handler.setValue(field, instance, 123.toShort())
                "intField" -> handler.setValue(field, instance, 456)
                "longField" -> handler.setValue(field, instance, 789L)
                "floatField" -> handler.setValue(field, instance, 12.34f)
                "doubleField" -> handler.setValue(field, instance, 56.78)
                "byteField" -> handler.setValue(field, instance, 9.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals(123.toShort(), instance.shortField)
        assertEquals(456.toShort(), instance.intField)
        assertEquals(789.toShort(), instance.longField)
        assertEquals(12.34f.toInt().toShort(), instance.floatField)
        assertEquals(56.78.toInt().toShort(), instance.doubleField)
        assertEquals(9.toShort(), instance.byteField)
    }

}
