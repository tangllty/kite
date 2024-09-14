package com.tang.jkorm.sql.provider

import com.tang.jkorm.sql.SqlStatement
import java.lang.reflect.Field

/**
 * SQL provider
 *
 * @author Tang
 */
interface SqlProvider {

    fun selectiveStrategy(any: Any?): Boolean

    fun getSql(sql: StringBuilder): String

    fun appendColumns(sql: StringBuilder, fieldList: List<Field>)

    fun <T> appendWhere(sql: StringBuilder, parameters: MutableList<Any?>, clazz: Class<T>, entity: Any)

    fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long)

    fun insert(entity: Any): SqlStatement

    fun insertSelective(entity: Any): SqlStatement

    fun batchInsert(entities: Iterable<Any>): SqlStatement

    fun batchInsertSelective(entities: Iterable<Any>): SqlStatement

    fun update(entity: Any): SqlStatement

    fun update(entity: Any, where: Any): SqlStatement

    fun updateSelective(entity: Any): SqlStatement

    fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement

    fun <T> select(clazz: Class<T>, entity: Any?): SqlStatement

    fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement

    fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): SqlStatement

}
