package com.tang.kite.sql

import kotlin.test.assertEquals

/**
 * @author Tang
 */
interface SqlProviderTest {

    fun equals(expected: Any, actual: Any) {
        assertEquals(expected, actual)
    }

    fun providerType()

    fun insert()

    fun insertSelective()

    fun batchInsert()

    fun batchInsertSelective()

    fun update()

    fun updateCondition()

    fun updateSelective()

    fun delete()

    fun deleteById()

    fun select()

    fun selectCondition()

    fun selectOrderBy()

    fun count()

    fun countCondition()

    fun paginate()

    fun paginateCondition()

    fun paginateOrderBy()

}
