package com.tang.jkorm.session.defaults

import com.tang.jkorm.config.DefaultConfig
import com.tang.jkorm.constants.BaseMethodName
import com.tang.jkorm.executor.Executor
import com.tang.jkorm.paginate.Page
import com.tang.jkorm.proxy.MapperProxyFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.sql.provider.SqlProvider
import com.tang.jkorm.utils.Reflects
import org.slf4j.LoggerFactory
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

    private fun getSecondArg(args: Array<out Any>?): Any {
        return args?.get(1) ?: throw IllegalArgumentException("Second parameter is null")
    }

    private fun getThirdArg(args: Array<out Any>?): Any {
        return args?.get(2) ?: throw IllegalArgumentException("Third parameter is null")
    }

    @Deprecated("This function has unchecked cast warning and I don't know how to fix it.")
    private fun <M, T> getGenericType(mapperInterface: Class<M>): Class<T> {
        val baseMapper = mapperInterface.genericInterfaces[0]
        val type = (baseMapper as ParameterizedType).actualTypeArguments[0]
        @Suppress("UNCHECKED_CAST")
        return type as Class<T>
    }

    private fun parameterToString(parameter: Any?): String {
        return parameter?.let { "$it(${it.javaClass.simpleName})" } ?: null.toString()
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Long) {
        val logger = LoggerFactory.getLogger(mapperInterface.canonicalName + "." + method.name)
        val preparing = "==>  Preparing: ${sqlStatement.sql}"
        val parameters = "==> Parameters: ${sqlStatement.parameters.joinToString { parameterToString(it) }}"
        val total = "<==      Total: $rows"
        val updates = "<==    Updates: $rows"
        val result = if (sqlStatement.sql.lowercase().startsWith("update")) updates else total
        logger.debug(preparing)
        logger.debug(parameters)
        logger.debug(result)
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Int) {
        log(method, mapperInterface, sqlStatement, rows.toLong())
    }

    private fun returnRows(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Long): Long {
        log(method, mapperInterface, sqlStatement, rows)
        return rows
    }

    private fun returnRows(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Int): Int {
        log(method, mapperInterface, sqlStatement, rows)
        return rows
    }

    override fun <T> execute(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any? {
        val type: Class<T> = getGenericType(mapperInterface)
        return when {
            BaseMethodName.isInsert(method) -> insert(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isInsertSelective(method) -> insertSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isBatchInsert(method) -> batchInsert(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isBatchInsertSelective(method) -> batchInsertSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdate(method) -> update(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdateCondition(method) -> update(method, mapperInterface, getFirstArg(args), getSecondArg(args))
            BaseMethodName.isUpdateSelective(method) -> updateSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isDelete(method) -> delete(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isDeleteById(method) -> deleteById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelect(method) -> selectList(method, mapperInterface, type, args?.first())
            BaseMethodName.isSelectById(method) -> selectById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isCount(method) -> count(method, mapperInterface, type, args?.first())
            BaseMethodName.isPaginate(method) -> processPaginate(method, mapperInterface, type, args)
            else -> throw IllegalArgumentException("Unknown method: ${method.name}")
        }
    }

    override fun <T> insert(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val insert = sqlProvider.insert(parameter)
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows)
    }

    override fun <T> insertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val insert = sqlProvider.insertSelective(parameter)
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows)
    }

    private fun asIterable(arg: Any): Iterable<Any> {
        val its = when (arg) {
            is Iterable<*> -> arg
            is Array<*> -> arg.toList()
            else -> throw IllegalArgumentException("Unsupported type: ${arg.javaClass.simpleName}")
        }
        return its.map { it as Any }
    }

    override fun <T> batchInsert(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val insert = sqlProvider.batchInsert(asIterable(parameter))
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows)
    }

    override fun <T> batchInsertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val insert = sqlProvider.batchInsertSelective(asIterable(parameter))
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows)
    }

    override fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val update = sqlProvider.update(parameter)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows)
    }

    override fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any, condition: Any): Int {
        val update = sqlProvider.update(parameter, condition)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows)
    }

    override fun <T> updateSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val update = sqlProvider.updateSelective(parameter)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows)
    }

    override fun <T> delete(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int {
        val delete = sqlProvider.delete(type, parameter)
        val rows = executor.update(delete, parameter)
        return returnRows(method, mapperInterface, delete, rows)
    }

    override fun <T> deleteById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val delete = sqlProvider.delete(type, entity as Any)
        val rows = executor.update(delete, parameter)
        return returnRows(method, mapperInterface, delete, rows)
    }

    override fun <T> selectList(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?): List<T> {
        val select = sqlProvider.select(type, parameter)
        val result = executor.query(select, type)
        log(method, mapperInterface, select, result.size)
        return result
    }

    override fun <T> selectById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val list = selectList(method, mapperInterface, type, entity)
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        if (list.isEmpty()) {
            return null
        }
        return list.first()
    }

    override fun <T> count(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?): Long {
        val count = sqlProvider.count(type, parameter)
        val total = executor.count(count, Long::class.java)
        return returnRows(method, mapperInterface, count, total)
    }

    private fun <T> processPaginate(method: Method, mapperInterface: Class<T>, type: Class<T>, args: Array<out Any>?): Page<T> {
        if (args == null || args.isEmpty()) {
            return paginate(method, mapperInterface, type, DefaultConfig.PAGE_NUMBER, DefaultConfig.PAGE_SIZE, emptyArray(), null)
        }
        val pageNumber = args[0] as Long
        val pageSize = args[1] as Long
        if (args.size == 2) {
            return paginate(method, mapperInterface, type, pageNumber, pageSize, emptyArray(), null)
        }
        if (args[2] is Array<*>) {
            val orderBys = if (args.getOrNull(2) != null) {
                val orderByArray = args[2] as Array<*>
                orderByArray.filterIsInstance<Pair<String, Boolean>>().toTypedArray()
            } else emptyArray()
            val parameter = args.getOrNull(3)
            return paginate(method, mapperInterface, type, pageNumber, pageSize, orderBys, parameter)
        }
        val parameter = args.getOrNull(2)
        return paginate(method, mapperInterface, type, pageNumber, pageSize, emptyArray(), parameter)
    }

    private fun <T> reasonable(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long): Pair<Long, Long> {
        val count = count(method, mapperInterface, type, null)
        val totalPage = (count / pageSize).toInt() + if (count % pageSize == 0L) 0 else 1
        val reasonablePageNumber = if (pageNumber > totalPage) totalPage else if (pageNumber < 1) DefaultConfig.PAGE_NUMBER else pageNumber
        return Pair(reasonablePageNumber.toLong(), count)
    }

    override fun <T> paginate(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long, orderBys: Array<Pair<String, Boolean>>, parameter: Any?): Page<T> {
        val reasonable = reasonable(method, mapperInterface, type, pageNumber, pageSize)
        val reasonablePageNumber = reasonable.first
        val total = reasonable.second
        val paginate = sqlProvider.paginate(type, parameter, orderBys, reasonablePageNumber, pageSize)
        val list = executor.query(paginate, type)
        log(method, mapperInterface, paginate, list.size)
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
        if (!executor.getConnection().autoCommit) {
            rollback()
        }
        executor.close()
    }

}
