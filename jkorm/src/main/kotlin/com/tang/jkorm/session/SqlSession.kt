package com.tang.jkorm.session

import java.lang.reflect.Method

/**
 * @author Tang
 */
interface SqlSession {

    fun <T> getMapper(clazz: Class<T>): T

    fun isBaseMethod(method: Method): Boolean

    fun <T> execute(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any?

    fun insert(method: Method, parameter: Any): Int

    fun insertSelective(method: Method, parameter: Any): Int

    fun update(method: Method, parameter: Any): Int

    fun updateSelective(method: Method, parameter: Any): Int

    fun <T> delete(method: Method, type: Class<T>, parameter: Any): Int

    fun <T> deleteById(method: Method, type: Class<T>, parameter: Any): Int

    fun <T> selectList(method: Method, type: Class<T>, parameter: Any?): List<T>

    fun <T> selectById(method: Method, type: Class<T>, parameter: Any): T?

    fun commit()

    fun rollback()

    fun close()

}
