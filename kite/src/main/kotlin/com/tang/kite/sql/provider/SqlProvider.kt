package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.enumeration.SqlType
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import java.lang.reflect.Field

/**
 * SQL provider
 *
 * @author Tang
 */
interface SqlProvider {

    fun getInCondition(sql: String, field: String, values: Iterable<Any?>, withAlias: Boolean = false): SqlStatement

    fun getNestedSelect(sql: String, field: String, value: Iterable<Any?>, join: Join): SqlStatement

    fun getWhere(fields: List<Field>, entity: Any, sqlType: SqlType? = null): List<LogicalStatement>

    fun insert(entity: Any): SqlStatement

    fun insertSelective(entity: Any): SqlStatement

    fun insertValues(entities: Iterable<Any>): SqlStatement

    fun batchInsert(entities: Iterable<Any>): BatchSqlStatement

    fun batchInsertSelective(entities: Iterable<Any>): List<SqlStatement>

    fun update(entity: Any): SqlStatement

    fun update(entity: Any, where: Any): SqlStatement

    fun updateSelective(entity: Any): SqlStatement

    fun updateSelective(entity: Any, where: Any): SqlStatement

    fun batchUpdate(entities: Iterable<Any>): BatchSqlStatement

    fun batchUpdateSelective(entities: Iterable<Any>): List<SqlStatement>

    fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement

    fun <T> deleteByIds(clazz: Class<T>, ids: Iterable<Any>): SqlStatement

    fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement

    fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean = true): SqlStatement

    fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement

    fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement

    fun <T> paginateWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long, withAlias: Boolean = true): SqlStatement

}
