package com.tang.jkorm.session

import com.tang.jkorm.paginate.OrderItem
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

    fun <T> insert(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> insertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> batchInsert(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> batchInsertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any, condition: Any): Int

    fun <T> updateSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> delete(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> deleteById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> selectList(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T>

    fun <T> selectById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T?

    fun <T> count(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?): Long

    fun <T> paginate(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T>

    fun commit()

    fun rollback()

    override fun close()

}
