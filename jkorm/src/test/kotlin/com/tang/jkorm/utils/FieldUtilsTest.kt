package com.tang.jkorm.utils

import com.tang.jkorm.session.entity.Account
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class FieldUtilsTest {

    @Test
    fun getFieldName() {
        val fieldName = FieldUtils.getFieldName(Account::id)
        assertEquals("id", fieldName)
    }

    @Test
    fun getField() {
        val field = FieldUtils.getField(Account::id)
        assertEquals("id", field.name)
    }
}
