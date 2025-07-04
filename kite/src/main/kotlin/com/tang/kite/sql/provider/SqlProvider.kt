package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.SqlStatement
import java.lang.reflect.Field

/**
 * SQL provider
 *
 * @author Tang
 */
interface SqlProvider {

    fun providerType(): ProviderType

    fun selectiveStrategy(any: Any?): Boolean

    fun getSql(sql: StringBuilder): String

    fun getInCondition(sql: String, field: String, values: Iterable<Any?>, withAlias: Boolean = false): SqlStatement

    fun getNestedSelect(sql: String, field: String, value: Iterable<Any?>, join: Join): SqlStatement

    fun getColumns(fieldList: List<Field>, withAlias: Boolean = false): String

    fun <T> getWhere(parameters: MutableList<Any?>, clazz: Class<T>, entity: Any?, withAlias: Boolean = false): String

    fun <T> getOrderBy(orderBys: Array<OrderItem<T>>, withAlias: Boolean = false): String

    fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String

    fun insert(entity: Any): SqlStatement

    fun insertSelective(entity: Any): SqlStatement

    fun batchInsert(entities: Iterable<Any>): SqlStatement

    fun batchInsertSelective(entities: Iterable<Any>): SqlStatement

    fun update(entity: Any): SqlStatement

    fun update(entity: Any, where: Any): SqlStatement

    fun updateSelective(entity: Any): SqlStatement

    fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement

    fun <T> deleteByIds(clazz: Class<T>, ids: Iterable<Any>): SqlStatement

    fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement

    fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean = true): SqlStatement

    fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement

    fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement

}
