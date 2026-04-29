package com.tang.kite.session.defaults

import com.tang.kite.annotation.Delete
import com.tang.kite.annotation.Insert
import com.tang.kite.annotation.Select
import com.tang.kite.annotation.Update
import com.tang.kite.config.PageConfig
import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.BaseMethodName
import com.tang.kite.constants.SqlString
import com.tang.kite.datasource.withDataSource
import com.tang.kite.enumeration.MethodType
import com.tang.kite.executor.ExecutionResult
import com.tang.kite.executor.defaults.DefaultExecutorFactory
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.paginate.OrderItem
import com.tang.kite.paginate.Page
import com.tang.kite.proxy.MapperProxyFactory
import com.tang.kite.session.DurationValue
import com.tang.kite.session.SqlSession
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.provider.SqlNodeProvider
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.transaction.Transaction
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.setValue
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.sql.Connection
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.nanoseconds

/**
 * @author Tang
 */
class DefaultSqlSession(

    transaction: Transaction,

    private val sqlDialect: SqlDialect

) : SqlSession {

    private val executor = DefaultExecutorFactory().newExecutor(transaction)

    private val provider = SqlNodeProvider(sqlDialect)

    private var isDirty = false

    override fun getConnection(): Connection {
        return executor.getConnection()
    }

    override fun <M : BaseMapper<T>, T : Any> getMapper(clazz: Class<M>): M {
        val mapperProxyFactory = MapperProxyFactory(clazz)
        return mapperProxyFactory.newInstance(this)
    }

    override fun <M : BaseMapper<T>, T : Any> getMapper(clazz: KClass<M>): M {
        return getMapper(clazz.java)
    }

    private fun nanoTime(): Long {
        return System.nanoTime()
    }

    private fun Long.elapsed(): Long {
        return nanoTime() - this
    }

    private fun getFirstArg(args: Array<out Any>?): Any {
        return args?.first() ?: throw IllegalArgumentException("Fist parameter is null")
    }

    private fun getSecondArg(args: Array<out Any>?): Any {
        return args?.get(1) ?: throw IllegalArgumentException("Second parameter is null")
    }

    private fun asIterable(arg: Any): Iterable<Any> {
        val its = when (arg) {
            is Iterable<*> -> arg
            is Array<*> -> arg.toList()
            else -> throw IllegalArgumentException("Argument is not an Iterable or Array: ${arg.javaClass.simpleName}")
        }
        return its.map { it as Any }
    }

    private fun asInt(arg: Any): Int {
        if (arg is Int) {
            return arg
        }
        throw IllegalArgumentException("Argument is not an Int: ${arg.javaClass.simpleName}")
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

    private fun getMapperLogger(mapperInterface: Class<*>, method: Method): Logger {
        return LoggerFactory.getLogger(mapperInterface.canonicalName + "." + method.name)
    }

    private fun logSqlInfo(logger: Logger, message: String) {
        logger.debug(message)
    }

    private fun logPreparing(logger: Logger, sql: String) {
        val preparing = "==>  Preparing: $sql"
        logSqlInfo(logger, preparing)
    }

    private fun logParameters(logger: Logger, parameters: List<Any?>) {
        val parameters = "==> Parameters: ${parameters.joinToString { parameterToString(it) }}"
        logSqlInfo(logger, parameters)
    }

    private fun logTotal(logger: Logger, rows: Long) {
        val total = "<==      Total: $rows"
        logSqlInfo(logger, total)
    }

    private fun logUpdates(logger: Logger, rows: Long) {
        val updates = "<==    Updates: $rows"
        logSqlInfo(logger, updates)
    }

    private fun logTotalOrUpdates(logger: Logger, sql: String, rows: Long) {
        val isSelect = getIsSelect(sql)
        if (isSelect.not()) {
            isDirty = true
        }
        if (isSelect) {
            logTotal(logger, rows)
        } else {
            logUpdates(logger, rows)
        }
    }

    private fun getIsSelect(sql: String): Boolean {
        return sql.trim().startsWith(SqlString.SELECT, ignoreCase = true)
    }

    private fun logPrepare(logger: Logger, duration: Long) {
        if (SqlConfig.prepareLogging.not()) {
            return
        }
        val prepare = "<==    Prepare: ${duration.nanoseconds.toString(SqlConfig.prepareUnit, SqlConfig.prepareDecimals)}"
        logSqlInfo(logger, prepare)
    }

    private fun logExecution(logger: Logger, duration: Long) {
        if (SqlConfig.executionLogging.not()) {
            return
        }
        val execution = "<==  Execution: ${duration.nanoseconds.toString(SqlConfig.executionUnit, SqlConfig.executionDecimals)}"
        logSqlInfo(logger, execution)
    }

    private fun logMapping(logger: Logger, duration: Long) {
        if (SqlConfig.mappingLogging.not()) {
            return
        }
        val mapping = "<==    Mapping: ${duration.nanoseconds.toString(SqlConfig.mappingUnit, SqlConfig.mappingDecimals)}"
        logSqlInfo(logger, mapping)
    }

    private fun logElapsed(logger: Logger, duration: Long) {
        if (SqlConfig.elapsedLogging.not()) {
            return
        }
        val elapsed = "<==    Elapsed: ${duration.nanoseconds.toString(SqlConfig.elapsedUnit, SqlConfig.elapsedDecimals)}"
        logSqlInfo(logger, elapsed)
    }

    private fun logDuration(logger: Logger, sql: String, duration: DurationValue) {
        if (SqlConfig.durationLogging.not()) {
            return
        }
        logPrepare(logger, duration.prepareTime)
        logExecution(logger, duration.executionTime)
        if (getIsSelect(sql)) {
            logMapping(logger, duration.mappingTime)
        }
        logElapsed(logger, duration.elapsedTime)
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatement: SqlStatement, rows: Long, duration: DurationValue) {
        if (SqlConfig.sqlLogging.not()) {
            return
        }
        val logger = getMapperLogger(mapperInterface, method)
        logPreparing(logger, sqlStatement.sql)
        logParameters(logger, sqlStatement.parameters)
        logTotalOrUpdates(logger, sqlStatement.sql, rows)
        logDuration(logger, sqlStatement.sql, duration)
    }

    private fun log(method: Method, mapperInterface: Class<*>, sqlStatementValue: SqlStatementValue<*>, rows: Int) {
        log(method, mapperInterface, sqlStatementValue.sqlStatement, rows.toLong(), sqlStatementValue.duration)
    }

    private fun returnRows(method: Method, mapperInterface: Class<*>, sqlStatementValue: SqlStatementValue<*>, rows: Long): Long {
        log(method, mapperInterface, sqlStatementValue.sqlStatement, rows, sqlStatementValue.duration)
        return rows
    }

    private fun log(method: Method, mapperInterface: Class<*>, batchSqlStatement: BatchSqlStatement, rows: Long, duration: DurationValue) {
        if (SqlConfig.sqlLogging.not()) {
            return
        }
        val logger = getMapperLogger(mapperInterface, method)
        logPreparing(logger, batchSqlStatement.sql)
        for (parameters in batchSqlStatement.parameters) {
            logParameters(logger, parameters)
        }
        logTotalOrUpdates(logger, batchSqlStatement.sql, rows)
        logDuration(logger, batchSqlStatement.sql, duration)
    }

    fun <T : Any> sqlStatementTemplate(prepare: () -> SqlStatement, execution: (SqlStatement) -> ExecutionResult<T>): SqlStatementValue<T> {
        val start = nanoTime()
        val sqlStatement = prepare.invoke()
        val prepareTime = start.elapsed()
        val result = execution.invoke(sqlStatement)
        val duration = result.toValue(prepareTime, start.elapsed())
        return SqlStatementValue(sqlStatement, result, duration)
    }

    fun <T: Any> returnRowsSqlStatementTemplate(method: Method, mapperInterface: Class<*>, prepare: () -> SqlStatement, execution: (SqlStatement) -> ExecutionResult<T>): Int {
        val value = sqlStatementTemplate(prepare, execution)
        val rowsInt = asInt(value.result.data)
        log(method, mapperInterface, value.sqlStatement, rowsInt.toLong(), value.duration)
        return rowsInt
    }

    fun <T : Any> batchSqlStatementTemplate(prepare: () -> BatchSqlStatement, execution: (BatchSqlStatement) -> ExecutionResult<T>): BatchSqlStatementValue<T> {
        val start = nanoTime()
        val batchSqlStatement = prepare.invoke()
        val prepareTime = start.elapsed()
        val result = execution.invoke(batchSqlStatement)
        val duration = result.toValue(prepareTime, start.elapsed())
        return BatchSqlStatementValue(batchSqlStatement, result, duration)
    }

    fun <T: Any> returnRowsBatchSqlStatementTemplate(method: Method, mapperInterface: Class<*>, prepare: () -> BatchSqlStatement, execution: (BatchSqlStatement) -> ExecutionResult<T>): Int {
        val value = batchSqlStatementTemplate(prepare, execution)
        val rowsInt = asInt(value.result.data)
        log(method, mapperInterface, value.batchSqlStatement, rowsInt.toLong(), value.duration)
        return rowsInt
    }

    override fun <M : BaseMapper<T>, T : Any> execute(methodType: MethodType, method: Method, args: Array<out Any>?, mapperInterface: Class<M>): Any? {
        val type: Class<T> = Reflects.getGenericType(mapperInterface)
        val dataSourceKey = Reflects.getDataSourceKey(mapperInterface, method, type)
        if (dataSourceKey.isNullOrBlank()) {
            return execute(methodType, method, args, mapperInterface, type)
        }
        withDataSource(dataSourceKey) {
            return execute(methodType, method, args, mapperInterface, type)
        }
    }

    private fun <M: BaseMapper<T>, T: Any> execute(methodType: MethodType, method: Method, args: Array<out Any>?, mapperInterface: Class<M>, type: Class<T>): Any? {
        return when (methodType) {
            MethodType.BASE -> baseExecutor(method, args, mapperInterface, type)
            MethodType.ANNOTATED -> annotatedMethodsInvoker(method, args, mapperInterface, type)
        }
    }

    private fun <M : BaseMapper<T>, T : Any> baseExecutor(method: Method, args: Array<out Any>?, mapperInterface: Class<M>, type: Class<T>): Any? {
        return when {
            BaseMethodName.isInsert(method) -> insert(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isInsertSelective(method) -> insertSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isInsertValues(method) -> insertValues(method, mapperInterface, getFirstArg(args), asInt(getSecondArg(args)))
            BaseMethodName.isBatchInsert(method) -> batchInsert(method, mapperInterface, getFirstArg(args), asInt(getSecondArg(args)))
            BaseMethodName.isUpdate(method) -> update(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdateCondition(method) -> update(method, mapperInterface, getFirstArg(args), getSecondArg(args))
            BaseMethodName.isUpdateSelective(method) -> updateSelective(method, mapperInterface, getFirstArg(args))
            BaseMethodName.isUpdateSelectiveCondition(method) -> updateSelective(method, mapperInterface, getFirstArg(args), getSecondArg(args))
            BaseMethodName.isUpdateWrapper(method) -> updateWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isBatchUpdate(method) -> batchUpdate(method, mapperInterface, getFirstArg(args), asInt(getSecondArg(args)))
            BaseMethodName.isDelete(method) -> delete(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isDeleteById(method) -> deleteById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isDeleteByIds(method) -> deleteByIds(method, mapperInterface, type, asIterable(getFirstArg(args)))
            BaseMethodName.isDeleteWrapper(method) -> deleteWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelect(method) -> processSelect(method, mapperInterface, type, args)
            BaseMethodName.isQueryWrapper(method) -> queryWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelectById(method) -> selectById(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelectOneWrapper(method) -> selectOneWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isSelectWithJoins(method) -> processSelectWithJoins(method, mapperInterface, type, args)
            BaseMethodName.isSelectByIdWithJoins(method) -> selectByIdWithJoins(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isCount(method) -> count(method, mapperInterface, type, args?.first())
            BaseMethodName.isCountWrapper(method) -> countWrapper(method, mapperInterface, type, getFirstArg(args))
            BaseMethodName.isPaginate(method) -> processPaginate(method, mapperInterface, type, args)
            BaseMethodName.isPaginateWithJoins(method) -> processPaginateWithJoins(method, mapperInterface, type, args)
            else -> throw IllegalArgumentException("Unknown method: ${getMethodSignature(method)}")
        }
    }

    private fun <M : BaseMapper<T>, T : Any> annotatedMethodsInvoker(method: Method, args: Array<out Any>?, mapperInterface: Class<M>, type: Class<T>): Any {
        val isSelect = method.isAnnotationPresent(Select::class.java)
        val value = sqlStatementTemplate(
            prepare = {
                val select = method.getAnnotation(Select::class.java)
                val insert = method.getAnnotation(Insert::class.java)
                val update = method.getAnnotation(Update::class.java)
                val delete = method.getAnnotation(Delete::class.java)
                val sql = select?.value ?: insert?.value ?: update?.value ?: delete?.value
                annotatedMethodParameters(method, args, sql!!)
            },
            execution = {
                if (isSelect) {
                    executor.query(it, type)
                } else {
                    val parameter = getFirstArg(args)
                    executor.update(it, parameter)
                }
            }
        )
        if (isSelect) {
            val list = value.result.data as List<*>
            log(method, mapperInterface, value, list.size)
            return list
        }
        val rows = value.result.data as Int
        log(method, mapperInterface, value.sqlStatement, rows.toLong(), value.duration)
        return rows
    }

    private fun annotatedMethodParameters(method: Method, args: Array<out Any>?, sql: String): SqlStatement {
        val params = SqlConfig.sqlParser.buildParams(method.parameters, args)
        return SqlConfig.sqlParser.parse(sql, params)
    }

    private fun getMethodSignature(method: Method): String {
        val methodName = method.name
        val parameterTypes = method.parameterTypes.joinToString(", ") { it.simpleName }
        return "$methodName($parameterTypes)"
    }

    override fun <M : BaseMapper<T>, T : Any> insert(method: Method, mapperInterface: Class<M>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.insert(parameter) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> insertSelective(method: Method, mapperInterface: Class<M>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.insertSelective(parameter) },
            execution = { executor.update(it, parameter) }
        )
    }

    private fun processBatch(parameter: Any, batchSize: Int, action: (List<Any>) -> Int): Int {
        val iterable = asIterable(parameter)
        val chunks = iterable.chunked(batchSize)
        return chunks.sumOf { action(it) }
    }

    override fun <M : BaseMapper<T>, T : Any> insertValues(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int {
        return processBatch(parameter, batchSize) { list ->
            returnRowsSqlStatementTemplate(
                method,
                mapperInterface,
                prepare = { provider.insertValues(list) },
                execution = { executor.update(it, list) }
            )
        }
    }

    override fun <M : BaseMapper<T>, T : Any> batchInsert(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int {
        return processBatch(parameter, batchSize) { list ->
            returnRowsBatchSqlStatementTemplate(
                method,
                mapperInterface,
                prepare = { provider.batchInsert(list) },
                execution = { executor.update(it, list) }
            )
        }
    }

    override fun <M : BaseMapper<T>, T : Any> update(method: Method, mapperInterface: Class<M>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.update(parameter) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> update(method: Method, mapperInterface: Class<M>, parameter: Any, condition: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.update(parameter, condition) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> updateSelective(method: Method, mapperInterface: Class<M>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.updateSelective(parameter) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> updateSelective(method: Method, mapperInterface: Class<M>, parameter: Any, condition: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.updateSelective(parameter, condition) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> updateWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = {
                @Suppress("UNCHECKED_CAST")
                val updateWrapper = parameter as UpdateWrapper<*>
                updateWrapper.setTableClassIfNotSet(type)
                updateWrapper.setTableFillFields()
                updateWrapper.getSqlStatement()
            },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> batchUpdate(method: Method, mapperInterface: Class<M>, parameter: Any, batchSize: Int): Int {
        return processBatch(parameter, batchSize) { list ->
            returnRowsBatchSqlStatementTemplate(
                method,
                mapperInterface,
                prepare = { provider.batchUpdate(list) },
                execution = { executor.update(it, list) }
            )
        }
    }

    override fun <M : BaseMapper<T>, T : Any> delete(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.delete(type, parameter) },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> deleteById(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = {
                val entity = type.getDeclaredConstructor().newInstance()
                val idField = Reflects.getIdField(type)
                setValue(idField, entity, parameter)
                provider.delete(type, entity as Any)
            },
            execution = { executor.update(it, parameter) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> deleteByIds(method: Method, mapperInterface: Class<M>, type: Class<T>, ids: Iterable<Any>): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = { provider.deleteByIds(type, ids) },
            execution = { executor.update(it, ids) }
        )
    }

    override fun <M : BaseMapper<T>, T : Any> deleteWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Int {
        return returnRowsSqlStatementTemplate(
            method,
            mapperInterface,
            prepare = {
                @Suppress("UNCHECKED_CAST")
                val deleteWrapper = parameter as DeleteWrapper<*>
                deleteWrapper.setTableClassIfNotSet(type)
                deleteWrapper.setTableFillFields()
                deleteWrapper.getSqlStatement()
            },
            execution = { executor.update(it, parameter) }
        )
    }

    private fun <M : BaseMapper<T>, T : Any> processSelect(method: Method, mapperInterface: Class<M>, type: Class<T>, args: Array<out Any>?): List<T> {
        if (args.isNullOrEmpty()) {
            return selectList(method, mapperInterface, type, null, emptyArray())
        }
        if (args.size == 1) {
            if (args.first().javaClass.isArray) {
                return selectList(method, mapperInterface, type, null, toOrderBys(args.first()))
            }
            return selectList(method, mapperInterface, type, args.first(), emptyArray())
        }
        if (args.size == 2) {
            return selectList(method, mapperInterface, type, args.first(), toOrderBys(args.last()))
        }
        return selectList(method, mapperInterface, type, null, emptyArray())
    }

    override fun <M : BaseMapper<T>, T : Any> selectList(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T> {
        val value = sqlStatementTemplate(
            prepare = { provider.select(type, parameter, orderBys) },
            execution = { executor.query(it, type) }
        )
        log(method, mapperInterface, value, value.result.data.size)
        return value.result.data
    }

    override fun <M : BaseMapper<T>, T : Any> queryWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): List<T> {
        val value = sqlStatementTemplate(
            prepare = {
                @Suppress("UNCHECKED_CAST")
                val queryWrapper = parameter as QueryWrapper<*>
                queryWrapper.setTableClassIfNotSet(type)
                queryWrapper.setTableFillFields()
                queryWrapper.getSqlStatement(sqlDialect)
            },
            execution = { executor.query(it, type) }
        )
        log(method, mapperInterface, value, value.result.data.size)
        return value.result.data
    }

    override fun <M : BaseMapper<T>, T : Any> selectById(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        setValue(idField, entity, parameter)
        val list = selectList(method, mapperInterface, type, entity, emptyArray())
        return getOneFromList(list)
    }

    override fun <M : BaseMapper<T>, T : Any> selectOneWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T? {
        val list = queryWrapper(method, mapperInterface, type, parameter)
        return getOneFromList(list)
    }

    private fun <T> getOneFromList(list: List<T>): T? {
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        return list.firstOrNull()
    }

    private fun <M : BaseMapper<T>, T : Any> processSelectWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, args: Array<out Any>?): List<T> {
        if (args.isNullOrEmpty()) {
            return selectListWithJoins(method, mapperInterface, type, null, emptyArray())
        }
        if (args.size == 1) {
            if (args.first().javaClass.isArray) {
                return selectListWithJoins(method, mapperInterface, type, null, toOrderBys(args.first()))
            }
            return selectListWithJoins(method, mapperInterface, type, args.first(), emptyArray())
        }
        if (args.size == 2) {
            return selectListWithJoins(method, mapperInterface, type, args.first(), toOrderBys(args.last()))
        }
        return selectListWithJoins(method, mapperInterface, type, null, emptyArray())
    }

    override fun <M : BaseMapper<T>, T : Any> selectListWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?, orderBys: Array<OrderItem<T>>): List<T> {
        val value = sqlStatementTemplate(
            prepare = { provider.selectWithJoins(type, parameter, orderBys) },
            execution = { executor.query(it, type) }
        )
        val list = value.result.data
        log(method, mapperInterface, value, list.size)
        val joins = Reflects.getIterableJoins(type)
        if (joins.isEmpty()) {
            return list
        }
        populateJoins(list, joins, type, method, mapperInterface)
        return list
    }

    private fun <M : BaseMapper<T>, T : Any> populateJoins(list: List<T>, joins: List<Field>, type: Class<T>, method: Method, mapperInterface: Class<M>) {
        list.forEach { entity ->
            joins.forEach { join ->
                val joinType = (join.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>
                val value = sqlStatementTemplate(
                    prepare = { provider.populateJoins(join, type, entity, joinType) },
                    execution = { statement -> executor.query(statement, joinType) }
                )
                val joinList = value.result.data
                log(method, mapperInterface, value, joinList.size)
                Reflects.makeAccessible(join, entity as Any)
                setValue(join, entity, joinList)
            }
        }
    }

    override fun <M : BaseMapper<T>, T : Any> selectByIdWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): T? {
        val entity = type.getDeclaredConstructor().newInstance()
        val idField = Reflects.getIdField(type)
        setValue(idField, entity, parameter)
        val list = selectListWithJoins(method, mapperInterface, type, entity, emptyArray())
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        if (list.isEmpty()) {
            return null
        }
        return list.first()
    }

    override fun <M : BaseMapper<T>, T : Any> count(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any?): Long {
        val value = sqlStatementTemplate(
            prepare = { provider.count(type, parameter) },
            execution = { executor.count(it, Long::class.java) }
        )
        return returnRows(method, mapperInterface, value, value.result.data)
    }

    override fun <M : BaseMapper<T>, T : Any> countWrapper(method: Method, mapperInterface: Class<M>, type: Class<T>, parameter: Any): Long {
        val value = sqlStatementTemplate(
            prepare = {
                @Suppress("UNCHECKED_CAST")
                val countWrapper = parameter as QueryWrapper<*>
                countWrapper.setTableClassIfNotSet(type)
                countWrapper.setTableFillFields()
                countWrapper.getSqlStatement(sqlDialect)
            },
            execution = { executor.count(it, type) }
        )
        return returnRows(method, mapperInterface, value, value.result.data)
    }

    private fun <M : BaseMapper<T>, T : Any> processPaginate(method: Method, mapperInterface: Class<M>, type: Class<T>, args: Array<out Any>?): Page<T> {
        if (args.isNullOrEmpty()) {
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

    private fun <M : BaseMapper<T>, T : Any> reasonable(method: Method, mapperInterface: Class<M>, type: Class<T>, pageNumber: Long, pageSize: Long): Pair<Long, Long> {
        val count = count(method, mapperInterface, type, null)
        val totalPage = (count / pageSize).toInt() + if (count % pageSize == 0L) 0 else 1
        val reasonablePageNumber = if (pageNumber > totalPage) totalPage else if (pageNumber < 1) PageConfig.pageNumber else pageNumber
        return Pair(reasonablePageNumber.toLong(), count)
    }

    override fun <M : BaseMapper<T>, T : Any> paginate(method: Method, mapperInterface: Class<M>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T> {
        val (reasonablePageNumber, total) = reasonable(method, mapperInterface, type, pageNumber, pageSize)
        val value = sqlStatementTemplate(
            prepare = { provider.paginate(type, parameter, orderBys, reasonablePageNumber, pageSize) },
            execution = { executor.query(it, type) }
        )
        log(method, mapperInterface, value, value.result.data.size)
        return Page(value.result.data, total, reasonablePageNumber, pageSize)
    }

    private fun <M : BaseMapper<T>, T : Any> processPaginateWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, args: Array<out Any>?): Page<T> {
        if (args.isNullOrEmpty()) {
            return paginateWithJoins(method, mapperInterface, type, PageConfig.pageNumber, PageConfig.pageSize, null, emptyArray())
        }
        val pageNumber = args[0] as Long
        val pageSize = args[1] as Long
        if (args.size == 2) {
            return paginateWithJoins(method, mapperInterface, type, pageNumber, pageSize, null, emptyArray())
        }
        if (!args[2].javaClass.isArray) {
            return paginateWithJoins(method, mapperInterface, type, pageNumber, pageSize, args[2], emptyArray())
        }
        val orderByArray = args[2] as Array<*>
        val orderBys = orderByArray.filterIsInstance<OrderItem<T>>().toTypedArray()
        val parameter = args.getOrNull(3)
        return paginateWithJoins(method, mapperInterface, type, pageNumber, pageSize, parameter, orderBys)
    }

    override fun <M : BaseMapper<T>, T : Any> paginateWithJoins(method: Method, mapperInterface: Class<M>, type: Class<T>, pageNumber: Long, pageSize: Long, parameter: Any?, orderBys: Array<OrderItem<T>>): Page<T> {
        val (reasonablePageNumber, total) = reasonable(method, mapperInterface, type, pageNumber, pageSize)
        val value = sqlStatementTemplate(
            prepare = { provider.paginateWithJoins(type, parameter, orderBys, reasonablePageNumber, pageSize) },
            execution = { executor.query(it, type) }
        )
        val list = value.result.data
        log(method, mapperInterface, value, list.size)
        val joins = Reflects.getIterableJoins(type)
        if (joins.isEmpty()) {
            return Page(list, total, reasonablePageNumber, pageSize)
        }
        populateJoins(list, joins, type, method, mapperInterface)
        return Page(list, total, reasonablePageNumber, pageSize)
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
        if (!getConnection().autoCommit && isDirty) {
            rollback()
        }
        executor.close()
        isDirty = false
    }

}
