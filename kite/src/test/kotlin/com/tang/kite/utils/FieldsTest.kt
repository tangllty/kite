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
}
