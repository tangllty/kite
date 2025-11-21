package com.tang.kite.result.primitive

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class ByteResultHandlerTest {

    data class ByteData(
        var nullField: Byte? = null,
        var byteField: Byte? = null,
        var intField: Byte? = null,
        var shortField: Byte? = null,
        var longField: Byte? = null,
        var floatField: Byte? = null,
        var doubleField: Byte? = null,
    )

    @Test
    fun setValue() {
        val handler = ByteResultHandler()
        val instance = ByteData()
        val fields = ByteData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "byteField" -> handler.setValue(field, instance, 7.toByte())
                "intField" -> handler.setValue(field, instance, 123)
                "shortField" -> handler.setValue(field, instance, 45.toShort())
                "longField" -> handler.setValue(field, instance, 67L)
                "floatField" -> handler.setValue(field, instance, 89.01f)
                "doubleField" -> handler.setValue(field, instance, 12.34)
            }
        }
        assertNull(instance.nullField)
        assertEquals(7.toByte(), instance.byteField)
        assertEquals(123.toByte(), instance.intField)
        assertEquals(45.toByte(), instance.shortField)
        assertEquals(67.toByte(), instance.longField)
        assertEquals(89.01f.toInt().toByte(), instance.floatField)
        assertEquals(12.34.toInt().toByte(), instance.doubleField)
    }

}
