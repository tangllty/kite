package com.tang.jkorm.session

import com.tang.jkorm.paginate.Page
import java.lang.reflect.Method

/**
 * SQL session
 *
 * @author Tang
 */
interface SqlSession : AutoCloseable {

    fun <T> getMapper(clazz: Class<T>): T

    fun isBaseMethod(method: Method): Boolean

    fun <T> execute(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any?

    fun insert(method: Method, parameter: Any): Int

    fun insertSelective(method: Method, parameter: Any): Int

    fun update(method: Method, parameter: Any): Int

    fun update(method: Method, parameter: Any, condition: Any): Int

    fun updateSelective(method: Method, parameter: Any): Int

    fun <T> delete(method: Method, type: Class<T>, parameter: Any): Int

    fun <T> deleteById(method: Method, type: Class<T>, parameter: Any): Int

    fun <T> selectList(method: Method, type: Class<T>, parameter: Any?): List<T>

    fun <T> selectById(method: Method, type: Class<T>, parameter: Any): T?

    fun <T> count(method: Method, type: Class<T>, parameter: Any?): Long

    fun <T> paginate(method: Method, type: Class<T>, pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>, parameter: Any?): Page<T>

    fun commit()

    fun rollback()

    override fun close()

}
