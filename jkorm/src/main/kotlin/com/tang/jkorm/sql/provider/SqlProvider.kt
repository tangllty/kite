package com.tang.jkorm.sql.provider

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

    fun appendValues(sql: StringBuilder, fieldList: List<Field>, entity: Any)

    fun appendSetValues(sql: StringBuilder, fieldList: List<Field>, entity: Any)

    fun <T> appendWhere(sql: StringBuilder, clazz: Class<T>, entity: Any)

    fun appendLimit(sql: StringBuilder, pageNumber: Long, pageSize: Long)

    fun getValue(value: Any): String

    fun getEqual(field: String, value: Any): String

    fun insert(entity: Any): String

    fun insertSelective(entity: Any): String

    fun update(entity: Any): String

    fun updateSelective(entity: Any): String

    fun <T> delete(clazz: Class<T>, entity: Any): String

    fun <T> select(clazz: Class<T>, entity: Any?): String

    fun <T> count(clazz: Class<T>, entity: Any?): String

    fun <T> paginate(type: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): String

}
