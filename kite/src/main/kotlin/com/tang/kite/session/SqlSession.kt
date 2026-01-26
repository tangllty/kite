package com.tang.kite.session

import com.tang.kite.enumeration.MethodType
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * SQL session
 *
 * @author Tang
 */
interface SqlSession : AutoCloseable {

    fun <M : BaseMapper<T>, T : Any> getMapper(clazz: Class<M>): M

    fun <M : BaseMapper<T>, T : Any> getMapper(clazz: KClass<M>): M

    fun <M : BaseMapper<T>, T : Any> execute(type: MethodType, method: Method, args: Array<out Any>?, mapperInterface: Class<M>): Any?

    fun <M : BaseMapper<T>, T : Any> insert(method: Method, mapperInterface: Class<M>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> insertSelective(method: Method, mapperInterface: Class<M>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> insertValues(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int

    fun <M : BaseMapper<T>, T : Any> batchInsert(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int

    fun <M : BaseMapper<T>, T : Any> update(method: Method, mapperInterface: Class<M>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> update(method: Method, mapperInterface: Class<M>, parameter: Any, condition: Any): Int

    fun <M : BaseMapper<T>, T : Any> updateSelective(method: Method, mapperInterface: Class<M>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> updateSelective(method: Method, mapperInterface: Class<M>, parameter: Any, condition: Any): Int

    fun <M : BaseMapper<T>, T : Any> updateWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> batchUpdate(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int

    fun <M : BaseMapper<T>, T : Any> delete(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> deleteById(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> deleteByIds(method: Method, mapperInterface: Class<M>, type: Class<T>, ids: Iterable<Any>): Int

    fun <M : BaseMapper<T>, T : Any> deleteWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int

    fun <M : BaseMapper<T>, T : Any> selectList(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T>

    fun <M : BaseMapper<T>, T : Any> queryWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): List<T>

    fun <M : BaseMapper<T>, T : Any> selectById(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T?

    fun <M : BaseMapper<T>, T : Any> selectOneWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T?

    fun <M : BaseMapper<T>, T : Any> selectListWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T>

    fun <M : BaseMapper<T>, T : Any> selectByIdWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T?

    fun <M : BaseMapper<T>, T : Any> count(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?): Long

    fun <M : BaseMapper<T>, T : Any> countWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Long

    fun <M : BaseMapper<T>, T : Any> paginate(method: Method, mapperInterface: Class<M>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T>

    fun <M : BaseMapper<T>, T : Any> paginateWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T>

    fun commit()

    fun rollback()

    override fun close()

}
