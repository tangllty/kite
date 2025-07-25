package com.tang.kite.result.primitive

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class BooleanResultHandlerTest {

    data class BooleanData(
        var nullField: Boolean? = null,
        var booleanField: Boolean? = null,
        var intField: Boolean? = null,
        var stringField: Boolean? = null,
    )

    @Test
    fun setValue() {
        val handler = BooleanResultHandler()
        val instance = BooleanData()
        val fields = BooleanData::class.java.declaredFields
        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> field.set(instance, null)
                "booleanField" -> handler.setValue(field, instance, true)
                "intField" -> handler.setValue(field, instance, 1)
                "stringField" -> handler.setValue(field, instance, "false")
            }
        }
        assertNull(instance.nullField)
        assertEquals(true, instance.booleanField)
        assertEquals(true, instance.intField)
        assertEquals(false, instance.stringField)
    }

}
