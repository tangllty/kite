package com.tang.kite.result.primitive

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class LongResultHandlerTest {

    data class LongData(
        var nullField: Long? = null,
        var longField: Long? = null,
        var intField: Long? = null,
        var shortField: Long? = null,
        var floatField: Long? = null,
        var doubleField: Long? = null,
        var byteField: Long? = null,
    )

    @Test
    fun setValue() {
        val handler = LongResultHandler()
        val instance = LongData()
        val fields = LongData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "longField" -> handler.setValue(field, instance, 123456789L)
                "intField" -> handler.setValue(field, instance, 123)
                "shortField" -> handler.setValue(field, instance, 45.toShort())
                "floatField" -> handler.setValue(field, instance, 67.89f)
                "doubleField" -> handler.setValue(field, instance, 12.34)
                "byteField" -> handler.setValue(field, instance, 7.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals(123456789L, instance.longField)
        assertEquals(123L, instance.intField)
        assertEquals(45L, instance.shortField)
        assertEquals(67.89f.toLong(), instance.floatField)
        assertEquals(12.34.toLong(), instance.doubleField)
        assertEquals(7L, instance.byteField)
    }

}
