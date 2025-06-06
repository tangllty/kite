package com.tang.kite.session.defaults

import com.tang.kite.annotation.Delete
import com.tang.kite.annotation.Insert
import com.tang.kite.annotation.Join
import com.tang.kite.annotation.Select
import com.tang.kite.annotation.Update
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.BaseMethodName
import com.tang.kite.enumeration.MethodType
import com.tang.kite.executor.Executor
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.proxy.MapperProxyFactory
import com.tang.kite.session.Configuration
import com.tang.kite.session.SqlSession
import com.tang.kite.sql.SqlStatement
import com.tang.kite.sql.provider.SqlProvider
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.parser.SqlParser
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import kotlin.time.Duration.Companion.nanoseconds

/**
 * @author Tang
 */
class DefaultSqlSession(

    private val configuration: Configuration,

    private val executor: Executor,

    private val sqlProvider: SqlProvider

) : SqlSession {

    private var isDirty = false

    override fun <T> getMapper(clazz: Class<T>): T {
        val mapperProxyFactory = MapperProxyFactory(clazz)
        return mapperProxyFactory.newInstance(this)
    }

    private fun getFirstArg(args: Array<out Any>?): Any {
        return args?.first() ?: throw IllegalArgumentException("Fist parameter is null")
    }

    private fun getSecondArg(args: Array<out Any>?): Any {
        return args?.get(1) ?: throw IllegalArgumentException("Second parameter is null")
    }

    private fun <T> toOrderBys(any: Any?): Array<OrderItem<T>> {
        if (any == null) {
            return emptyArray()
        }
        val orderByArray = any as Array<*>
        val orderBys = orderByArray.filterIsInstance<OrderItem<T>>().toTypedArray()
        return orderBys
    }

    private fun parameterToString(parameter: Any?): String {
        return parameter?.let { "$it(${it.javaClass.simpleName})" } ?: null.toString()
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Long, duration: Long) {
        if (SqlConfig.sqlLogging.not()) {
            return
        }
        val logger = LoggerFactory.getLogger(mapperInterface.canonicalName + "." + method.name)
        val preparing = "==>  Preparing: ${sqlStatement.sql}"
        val parameters = "==> Parameters: ${sqlStatement.parameters.joinToString { parameterToString(it) }}"
        val total = "<==      Total: $rows"
        val updates = "<==    Updates: $rows"
        val isSelect = sqlStatement.sql.trim().startsWith("SELECT", ignoreCase = true)
        val result = if (isSelect) total else updates
        if (isSelect.not()) {
            isDirty = true
        }
        logger.debug(preparing)
        logger.debug(parameters)
        logger.debug(result)
        if (SqlConfig.sqlDurationLogging.not()) {
            return
        }
        val unit = SqlConfig.durationUnit
        val decimals = SqlConfig.durationDecimals
        val execution = "<==  Execution: ${duration.nanoseconds.toString(unit, decimals)}"
        logger.debug(execution)
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Int, execution: Long) {
        log(method, mapperInterface, sqlStatement, rows.toLong(), execution)
    }

    private fun returnRows(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Long, execution: Long): Long {
        log(method, mapperInterface, sqlStatement, rows, execution)
        return rows
    }

    private fun returnRows(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Int, execution: Long): Int {
        log(method, mapperInterface, sqlStatement, rows, execution)
        return rows
    }

    override fun <T> execute(type: MethodType, method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any? {
        return when (type) {
            MethodType.BASE -> baseExecutor(method, args, mapperInterface)
            MethodType.ANNOTATED -> annotatedMethodsInvoker(method, args, mapperInterface)
        }
    }

    private fun <T> baseExecutor(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any? {
        val type: Class<T> = getGenericType(mapperInterface)
        return when {
            BaseMethodName.isInsert(method) -> insert(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isInsertSelective(method) -> insertSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isBatchInsert(method) -> batchInsert(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isBatchInsertSelective(method) -> batchInsertSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdate(method) -> update(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdateCondition(method) -> update(method, mapperInterface, getFirstArg(args), getSecondArg(args))
            BaseMethodName.isUpdateSelective(method) -> updateSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdateWrapper(method) -> updateWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isDelete(method) -> delete(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isDeleteById(method) -> deleteById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelect(method) -> processSelect(method, mapperInterface, type, args)
            BaseMethodName.isSelectById(method) -> selectById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelectWithJoins(method) -> processSelectWithJoins(method, mapperInterface, type, args)
            BaseMethodName.isSelectByIdWithJoins(method) -> selectByIdWithJoins(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isQueryWrapper(method) -> queryWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isCount(method) -> count(method, mapperInterface, type, args?.first())
            BaseMethodName.isPaginate(method) -> processPaginate(method, mapperInterface, type, args)
            else -> throw IllegalArgumentException("Unknown method: ${getMethodSignature(method)}")
        }
    }

    private fun <T> annotatedMethodsInvoker(method: Method, args: Array<out Any>?, mapperInterface: Class<T>): Any {
        val start = System.nanoTime()
        val select = method.getAnnotation(Select::class.java)
        val insert = method.getAnnotation(Insert::class.java)
        val update = method.getAnnotation(Update::class.java)
        val delete = method.getAnnotation(Delete::class.java)
        val sql = select?.value ?: insert?.value ?: update?.value ?: delete?.value

        val type: Class<T> = getGenericType(mapperInterface)
        val sqlStatement = annotatedMethodParameters(method, args, sql!!)

        if (select != null) {
            val list = executor.query(sqlStatement, type)
            log(method, mapperInterface, sqlStatement, list.size, System.nanoTime() - start)
            return list
        }
        val parameter = getFirstArg(args)
        val rows = executor.update(sqlStatement, parameter)
        return returnRows(method, mapperInterface, sqlStatement, rows, System.nanoTime() - start)
    }

    private fun annotatedMethodParameters(method: Method, args: Array<out Any>?, sql: String): SqlStatement {
        val paramValueMap = SqlParser.buildParamValueMap(method.parameters, args)
        return SqlParser.parse(sql, paramValueMap)
    }

    private fun <M, T> getGenericType(mapperInterface: Class<M>): Class<T> {
        val baseMapper = mapperInterface.genericInterfaces[0]
        val type = (baseMapper as ParameterizedType).actualTypeArguments[0]
        @Suppress("UNCHECKED_CAST")
        return type as Class<T>
    }

    private fun getMethodSignature(method: Method): String {
        val methodName = method.name
        val parameterTypes = method.parameterTypes.joinToString(", ") { it.simpleName }
        return "$methodName($parameterTypes)"
    }

    override fun <T> insert(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val insert = sqlProvider.insert(parameter)
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows, System.nanoTime() - start)
    }

    override fun <T> insertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val insert = sqlProvider.insertSelective(parameter)
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows, System.nanoTime() - start)
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
        val start = System.nanoTime()
        val insert = sqlProvider.batchInsert(asIterable(parameter))
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows, System.nanoTime() - start)
    }

    override fun <T> batchInsertSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val insert = sqlProvider.batchInsertSelective(asIterable(parameter))
        val rows = executor.update(insert, parameter)
        return returnRows(method, mapperInterface, insert, rows, System.nanoTime() - start)
    }

    override fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val update = sqlProvider.update(parameter)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows, System.nanoTime() - start)
    }

    override fun <T> update(method: Method, mapperInterface: Class<T>, parameter: Any, condition: Any): Int {
        val start = System.nanoTime()
        val update = sqlProvider.update(parameter, condition)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows, System.nanoTime() - start)
    }

    override fun <T> updateSelective(method: Method, mapperInterface: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val update = sqlProvider.updateSelective(parameter)
        val rows = executor.update(update, parameter)
        return returnRows(method, mapperInterface, update, rows, System.nanoTime() - start)
    }

    override fun <T> updateWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val updateWrapper = parameter as UpdateWrapper<*>
        val sqlStatement = updateWrapper.getSqlStatement()
        val rows = executor.update(sqlStatement, parameter)
        return returnRows(method, mapperInterface, sqlStatement, rows, System.nanoTime() - start)
    }

    override fun <T> delete(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val delete = sqlProvider.delete(type, parameter)
        val rows = executor.update(delete, parameter)
        return returnRows(method, mapperInterface, delete, rows, System.nanoTime() - start)
    }

    override fun <T> deleteById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): Int {
        val start = System.nanoTime()
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val delete = sqlProvider.delete(type, entity as Any)
        val rows = executor.update(delete, parameter)
        return returnRows(method, mapperInterface, delete, rows, System.nanoTime() - start)
    }

    private fun <T> processSelect(method: Method, mapperInterface: Class<T>, type: Class<T>, args: Array<out Any>?): List<T> {
        if (args == null || args.isEmpty()) {
            return selectList(method, mapperInterface, type, null, emptyArray())
        }
        if (args.size == 1) {
            if (args[0].javaClass.isArray) {
                return selectList(method, mapperInterface, type, null, toOrderBys(args[0]))
            }
            return selectList(method, mapperInterface, type, args[0], emptyArray())
        }
        if (args.size == 2) {
            return selectList(method, mapperInterface, type, args[0], toOrderBys(args[1]))
        }
        return selectList(method, mapperInterface, type, null, emptyArray())
    }

    override fun <T> selectList(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T> {
        val start = System.nanoTime()
        val select = sqlProvider.select(type, parameter, orderBys)
        val list = executor.query(select, type)
        log(method, mapperInterface, select, list.size, System.nanoTime() - start)
        return list
    }

    override fun <T> selectById(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val list = selectList(method, mapperInterface, type, entity, emptyArray())
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        if (list.isEmpty()) {
            return null
        }
        return list.first()
    }

    private fun <T> processSelectWithJoins(method: Method, mapperInterface: Class<T>, type: Class<T>, args: Array<out Any>?): List<T> {
        if (args == null || args.isEmpty()) {
            return selectListWithJoins(method, mapperInterface, type, null, emptyArray())
        }
        if (args.size == 1) {
            if (args[0].javaClass.isArray) {
                return selectListWithJoins(method, mapperInterface, type, null, toOrderBys(args[0]))
            }
            return selectListWithJoins(method, mapperInterface, type, args[0], emptyArray())
        }
        if (args.size == 2) {
            return selectListWithJoins(method, mapperInterface, type, args[0], toOrderBys(args[1]))
        }
        return selectListWithJoins(method, mapperInterface, type, null, emptyArray())
    }

    override fun <T> selectListWithJoins(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T> {
        val start = System.nanoTime()
        val select = sqlProvider.selectWithJoins(type, parameter, orderBys)
        val list = executor.query(select, type)
        log(method, mapperInterface, select, list.size, System.nanoTime() - start)
        val joins = Reflects.getIterableJoins(type)
        if (joins.isEmpty()) {
            return list
        }
        list.forEach {
            joins.forEach { join ->
                val joinStart = System.nanoTime()
                val joinType = (join.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>
                val joinAnnotation = join.getAnnotation(Join::class.java)!!
                val joinTable = joinAnnotation.joinTable
                val joinSelfField = joinAnnotation.joinSelfColumn
                val joinTargetField = joinAnnotation.joinTargetColumn
                val selfField = Reflects.getField(type, joinAnnotation.selfField)
                Reflects.makeAccessible(selfField!!, it as Any)
                val selfFieldValue = selfField.get(it)
                // TODO The parameter null could probably be replaced with a conditional parameter
                var joinSelect = sqlProvider.selectWithJoins(joinType, null, emptyArray())
                val joinSqlStatement = if (joinTable.isNotEmpty() && joinSelfField.isNotEmpty() && joinTargetField.isNotEmpty()) {
                    sqlProvider.getNestedSelect(joinSelect.sql, joinAnnotation.targetField, listOf(selfFieldValue), joinAnnotation)
                } else {
                    sqlProvider.getInCondition(joinSelect.sql, joinAnnotation.targetField, listOf(selfFieldValue))
                }
                val parameters = joinSelect.parameters.plus(joinSqlStatement.parameters).toMutableList()
                joinSelect = SqlStatement(joinSqlStatement.sql, parameters)
                val joinList = executor.query(joinSelect, joinType)
                log(method, mapperInterface, joinSelect, joinList.size, System.nanoTime() - joinStart)
                Reflects.makeAccessible(join, it as Any)
                join.set(it, joinList)
            }
        }
        return list
    }

    override fun <T> selectByIdWithJoins(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        Reflects.makeAccessible(idField, entity as Any)
        idField.set(entity, parameter)
        val list = selectListWithJoins(method, mapperInterface, type, entity, emptyArray())
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        if (list.isEmpty()) {
            return null
        }
        return list.first()
    }

    override fun <T> queryWrapper(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any): List<T> {
        val start = System.nanoTime()
        val queryWrapper = parameter as QueryWrapper<*>
        val sqlStatement = queryWrapper.getSqlStatement()
        val list = executor.query(sqlStatement, type)
        log(method, mapperInterface, sqlStatement, list.size, System.nanoTime() - start)
        return list
    }

    override fun <T> count(method: Method, mapperInterface: Class<T>, type: Class<T>, parameter: Any?): Long {
        val start = System.nanoTime()
        val count = sqlProvider.count(type, parameter)
        val total = executor.count(count, Long::class.java)
        return returnRows(method, mapperInterface, count, total, System.nanoTime() - start)
    }

    private fun <T> processPaginate(method: Method, mapperInterface: Class<T>, type: Class<T>, args: Array<out Any>?): Page<T> {
        if (args == null || args.isEmpty()) {
            return paginate(method, mapperInterface, type, PageConfig.pageNumber, PageConfig.pageSize, null, emptyArray())
        }
        val pageNumber = args[0] as Long
        val pageSize = args[1] as Long
        if (args.size == 2) {
            return paginate(method, mapperInterface, type, pageNumber, pageSize, null, emptyArray())
        }
        if (!args[2].javaClass.isArray) {
            return paginate(method, mapperInterface, type, pageNumber, pageSize, args[2], emptyArray())
        }
        val orderByArray = args[2] as Array<*>
        val orderBys = orderByArray.filterIsInstance<OrderItem<T>>().toTypedArray()
        val parameter = args.getOrNull(3)
        return paginate(method, mapperInterface, type, pageNumber, pageSize, parameter, orderBys)
    }

    private fun <T> reasonable(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long): Pair<Long, Long> {
        val count = count(method, mapperInterface, type, null)
        val totalPage = (count / pageSize).toInt() + if (count % pageSize == 0L) 0 else 1
        val reasonablePageNumber = if (pageNumber > totalPage) totalPage else if (pageNumber < 1) PageConfig.pageNumber else pageNumber
        return Pair(reasonablePageNumber.toLong(), count)
    }

    override fun <T> paginate(method: Method, mapperInterface: Class<T>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T> {
        val start = System.nanoTime()
        val reasonable = reasonable(method, mapperInterface, type, pageNumber, pageSize)
        val reasonablePageNumber = reasonable.first
        val total = reasonable.second
        val paginate = sqlProvider.paginate(type, parameter, orderBys, reasonablePageNumber, pageSize)
        val list = executor.query(paginate, type)
        log(method, mapperInterface, paginate, list.size, System.nanoTime() - start)
        val page = Page<T>()
        page.rows = list
        page.total = total
        page.pageNumber = reasonablePageNumber
        page.pageSize = pageSize
        return page
    }

    override fun commit() {
        executor.commit()
        isDirty = false
    }

    override fun rollback() {
        executor.rollback()
        isDirty = false
    }

    override fun close() {
        if (!executor.getConnection().autoCommit && isDirty) {
            rollback()
        }
        executor.close()
        isDirty = false
    }

}
