package com.tang.kite.utils

import com.tang.kite.session.entity.Account
import com.tang.kite.session.entity.Role
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class ReflectsTest {

    @Test
    fun getIdField() {
        val idField = Reflects.getIdField(Account::class.java)
        assertEquals("id", idField.name)
    }

    @Test
    fun isAutoIncrementId() {
        val isAutoIncrementId = Reflects.isAutoIncrementId(Account::class.java)
        assertEquals(true, isAutoIncrementId)
    }

    @Test
    fun getGeneratedIdByClass() {
        val generatedId = Reflects.getGeneratedId(Account::class.java)
        assertNotNull(generatedId)
    }

    @Test
    fun getTableName() {
        val tableName = Reflects.getTableName(Account::class.java)
        assertEquals("account", tableName)
    }

    @Test
    fun getFieldByName() {
        val field = Reflects.getField(Account::class.java, "id")
        assertEquals("id", field?.name)
    }

    @Test
    fun getColumnName() {
        val field = Reflects.getIdField(Account::class.java)
        val columnName = Reflects.getColumnName(field)
        assertEquals("id", columnName)
    }

    @Test
    fun getGeneratedId() {
        val idField = Reflects.getIdField(Role::class.java)
        val generatedId = Reflects.getGeneratedId(idField)
        assertNotNull(generatedId)
    }

    @Test
    fun getSqlFields() {
        val sqlFields = Reflects.getSqlFields(Account::class.java)
        assertTrue(sqlFields.none { it.name == "be_ignore" })
    }

    @Test
    fun getTableAlias() {
        val tableAlias = Reflects.getTableAlias(Account::class.java)
        assertEquals("a", tableAlias)
    }

    @Test
    fun getFieldName() {
        val fieldName = Reflects.getFieldName(Account::id)
        assertEquals("id", fieldName)
    }

    @Test
    fun getField() {
        val field = Reflects.getField(Account::id)
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

        val name = Reflects.getValue(map, "name")
        assertEquals("test", name)
        val age = Reflects.getValue(map, "details.age")
        assertEquals(30, age)
        val hobby2 = Reflects.getValue(map, "details.hobbies.hobby2")
        assertEquals("gaming", hobby2)
    }

}
