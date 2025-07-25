package com.tang.kite.result.primitive

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class CharResultHandlerTest {

    data class CharData(
        var nullField: Char? = null,
        var charField: Char? = null,
        var stringField: Char? = null,
        var intField: Char? = null,
        var shortField: Char? = null,
        var byteField: Char? = null,
    )

    @Test
    fun setValue() {
        val handler = CharResultHandler()
        val instance = CharData()
        val fields = CharData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "charField" -> handler.setValue(field, instance, 'A')
                "stringField" -> handler.setValue(field, instance, "B")
                "intField" -> handler.setValue(field, instance, 67)
                "shortField" -> handler.setValue(field, instance, 68.toShort())
                "byteField" -> handler.setValue(field, instance, 69.toByte())
            }
        }
        assertNull(instance.nullField)
        assertEquals('A', instance.charField)
        assertEquals('B', instance.stringField)
        assertEquals(67.toChar(), instance.intField)
        assertEquals(68.toChar(), instance.shortField)
        assertEquals(69.toChar(), instance.byteField)
    }

}
