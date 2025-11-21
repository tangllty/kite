package com.tang.kite.result

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Tang
 */
class EnumResultHandlerTest {

    data class EnumData(
        var nullField: TestEnum? = null,
        var enumField: TestEnum? = null,
        var nameField: TestEnum? = null,
        var ordinalField: TestEnum? = null
    )

    @Test
    fun setValue() {
        val handler = EnumResultHandler()
        val instance = EnumData()
        val fields = EnumData::class.java.declaredFields
        val expectedEnum = TestEnum.ONE
        val nameValue = "TWO"
        val ordinalValue = 2

        for (field in fields) {
            field.isAccessible = true
            when (field.name) {
                "nullField" -> handler.setNullValue(field, instance, null)
                "enumField" -> handler.setValue(field, instance, expectedEnum)
                "nameField" -> handler.setValue(field, instance, nameValue)
                "ordinalField" -> handler.setValue(field, instance, ordinalValue)
            }
        }

        assertNull(instance.nullField)
        assertEquals(expectedEnum, instance.enumField)
        assertEquals(TestEnum.TWO, instance.nameField)
        assertEquals(TestEnum.THREE, instance.ordinalField)
    }

}

enum class TestEnum(val value: Int) {
    ONE(1), TWO(2), THREE(3);
}
