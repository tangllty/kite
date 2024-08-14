package com.tang.jkorm.session.defaults

import com.tang.jkorm.config.DefaultConfig
import com.tang.jkorm.constants.BaseMethodName
import com.tang.jkorm.executor.Executor
import com.tang.jkorm.paginate.Page
import com.tang.jkorm.proxy.MapperProxyFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.sql.provider.SqlProvider
import com.tang.jkorm.utils.Reflects
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * @author Tang
 */
class DefaultSqlSession(

    private val configuration: Configuration,

    private val executor: Executor,

    private val sqlProvider: SqlProvider

) : SqlSession {

    override fun <T> getMapper(clazz: Class<T>): T {
        val mapperProxyFactory = MapperProxyFactory(clazz)
        return mapperProxyFactory.newInstance(this)
    }

    override fun isBaseMethod(method: Method): Boolean {
        return BaseMethodName.isBaseMethod(method.name)
    }

    private fun getFirstArg(args: Array<out Any>?): Any {
        return args?.first() ?: throw IllegalArgumentException("Fist parameter is null")
    }

    @Deprecated("This function has unchecked cast warning and I don't know how to fix it.")
    private fun <M, T> getGenericType(mapperInterface: Class<M>): Class<T> {
        val baseMapper = mapperInterface.genericInterfaces[0]
        val type = (baseMapper as ParameterizedType).actualTypeArguments[0]
        @Suppress("UNCHECKED_CAST")
        return type as Class<T>
    }

    override fun <T> execute(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any? {
        val type: Class<T> = getGenericType(mapperInterface)
        return when {
            BaseMethodName.isInsert(method) -> insert(method, getFirstArg(args))
            BaseMethodName.isInsertSelective(method) -> insertSelective(method, getFirstArg(args))
            BaseMethodName.isUpdate(method) -> update(method, getFirstArg(args))
            BaseMethodName.isUpdateSelective(method) -> updateSelective(method, getFirstArg(args))
            BaseMethodName.isDelete(method) -> delete(method, type, getFirstArg(args))
            BaseMethodName.isDeleteById(method) -> deleteById(method, type, getFirstArg(args))
            BaseMethodName.isSelect(method) -> selectList(method, type, args?.first())
            BaseMethodName.isSelectById(method) -> selectById(method, type, getFirstArg(args))
            BaseMethodName.isCount(method) -> count(method, type, args?.first())
            BaseMethodName.isPaginate(method) -> processPaginate(method, type, args)
            else -> throw IllegalArgumentException("Unknown method: ${method.name}")
        }
    }

    override fun insert(method: Method, parameter: Any): Int {
        val insert = sqlProvider.insert(parameter)
        return executor.update(insert, parameter)
    }

    override fun insertSelective(method: Method, parameter: Any): Int {
        val insert = sqlProvider.insertSelective(parameter)
        return executor.update(insert, parameter)
    }

    override fun update(method: Method, parameter: Any): Int {
        val update = sqlProvider.update(parameter)
        return executor.update(update, parameter)
    }

    override fun updateSelective(method: Method, parameter: Any): Int {
        val update = sqlProvider.updateSelective(parameter)
        return executor.update(update, parameter)
    }

    override fun <T> delete(method: Method, type: Class<T>, parameter: Any): Int {
        val delete = sqlProvider.delete(type, parameter)
        return executor.update(delete, parameter)
    }

    override fun <T> deleteById(method: Method, type: Class<T>, parameter: Any): Int {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val delete = sqlProvider.delete(type, entity as Any)
        return executor.update(delete, parameter)
    }

    override fun <T> selectList(method: Method, type: Class<T>, parameter: Any?): List<T> {
        val select = sqlProvider.select(type, parameter)
        return executor.query(select, type)
    }

    override fun <T> selectById(method: Method, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val list = selectList(method, type, entity)
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        if (list.isEmpty()) {
            return null
        }
        return list.first()
    }

    override fun <T> count(method: Method, type: Class<T>, parameter: Any?): Long {
        val count = sqlProvider.count(type, parameter)
        return executor.count(count, Long::class.java)
    }

    private fun <T> processPaginate(method: Method, type: Class<T>, args: Array<out Any>?): Page<T> {
        if (args == null || args.isEmpty()) {
            return paginate(method, type, DefaultConfig.PAGE_NUMBER, DefaultConfig.PAGE_SIZE, emptyArray(), null)
        }
        val pageNumber = args[0] as Long
        val pageSize = args[1] as Long
        val orderBys = if (args.getOrNull(2) != null) {
            val orderByArray = args[2] as Array<*>
            orderByArray.filterIsInstance<Pair<String, Boolean>>().toTypedArray()
        } else emptyArray()
        val parameter = args.getOrNull(3)
        return paginate(method, type, pageNumber, pageSize, orderBys, parameter)
    }

    private fun <T> reasonable(method: Method, type: Class<T>, pageNumber: Long, pageSize: Long): Pair<Long, Long> {
        val count = count(method, type, null)
        val totalPage = (count / pageSize).toInt() + if (count % pageSize == 0L) 0 else 1
        val reasonablePageNumber = if (pageNumber > totalPage) totalPage else if (pageNumber < 1) DefaultConfig.PAGE_NUMBER else pageNumber
        return Pair(reasonablePageNumber.toLong(), count)
    }

    override fun <T> paginate(method: Method, type: Class<T>, pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>, parameter: Any?): Page<T> {
        val reasonable = reasonable(method, type, pageNumber, pageSize)
        val reasonablePageNumber = reasonable.first
        val total = reasonable.second
        val paginate = sqlProvider.paginate(type, parameter, orderBys, reasonablePageNumber, pageSize)
        val list = executor.query(paginate, type)
        val page = Page<T>()
        page.rows = list
        page.total = total
        page.pageNumber = reasonablePageNumber
        page.pageSize = pageSize
        return page
    }

    override fun commit() {
        executor.commit()
    }

    override fun rollback() {
        executor.rollback()
    }

    override fun close() {
        rollback()
        executor.close()
    }

}
