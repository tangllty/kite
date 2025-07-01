package com.tang.kite.session

import com.tang.kite.enumeration.MethodType
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import java.lang.reflect.Method

/**
 * SQL session
 *
 * @author Tang
 */
interface SqlSession : AutoCloseable {

    fun <T> getMapper(clazz: Class<T>): T

    fun <T> execute(type: MethodType, method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any?

    fun <T> insert(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> insertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> batchInsert(method: Method, mapperInterface: Class<T>, parameter: Any, batchSize: Int): Int

    fun <T> batchInsertSelective(method: Method, mapperInterface: Class<T>, parameter: Any, batchSize: Int): Int

    fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any, condition: Any): Int

    fun <T> updateSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int

    fun <T> updateWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> delete(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> deleteById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> deleteWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int

    fun <T> selectList(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T>

    fun <T> selectListWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): List<T>

    fun <T> selectById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T?

    fun <T> selectOneWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T?

    fun <T> selectListWithJoins(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T>

    fun <T> selectByIdWithJoins(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T?

    fun <T> count(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?): Long

    fun <T> paginate(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T>

    fun commit()

    fun rollback()

    override fun close()

}
