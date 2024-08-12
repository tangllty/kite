package com.tang.jkorm.mapper

/**
 * @author Tang
 */
interface BaseMapper<T> {

    fun insert(type: T): Int

    fun insertSelective(type: T): Int

    fun update(type: T): Int

    fun updateSelective(type: T): Int

    fun delete(type: T): Int

    fun deleteById(id: Long): Int

    fun select(): List<T>

    fun select(type: T): List<T>

    fun selectById(id: Long): T?

    fun count(): Long

    fun count(type: T): Long

}
