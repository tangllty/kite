package com.tang.kite.utils

import com.tang.kite.session.entity.Account
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class FieldsTest {

    @Test
    fun getFieldName() {
        val fieldName = Fields.getFieldName(Account::id)
        assertEquals("id", fieldName)
    }

    @Test
    fun getField() {
        val field = Fields.getField(Account::id)
        assertEquals("id", field.name)
    }

    @Test
    fun getValue() {
        val map = mapOf(
            "id" to 1,
            "name" to "test",
            "details" to mapOf(
                "age" to 30,
                "address" to "123 Street",
                "hobbies" to mapOf(
                    "hobby1" to "reading",
                    "hobby2" to "gaming"
                )
            )
        )

        val name = Fields.getValue(map, "name")
        assertEquals("test", name)
        val age = Fields.getValue(map, "details.age")
        assertEquals(30, age)
        val hobby2 = Fields.getValue(map, "details.hobbies.hobby2")
        assertEquals("gaming", hobby2)
    }

}
