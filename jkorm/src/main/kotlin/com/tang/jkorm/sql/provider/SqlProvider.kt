package com.tang.jkorm.sql.provider

/**
 * SQL provider
 *
 * @author Tang
 */
interface SqlProvider {

    fun getSql(sql: StringBuilder): String

    fun insert(entity: Any): String

    fun insertSelective(entity: Any): String

    fun update(entity: Any): String

    fun updateSelective(entity: Any): String

    fun <T> delete(clazz: Class<T>, entity: Any): String

    fun <T> select(clazz: Class<T>, entity: Any?): String

    fun <T> count(clazz: Class<T>, entity: Any?): String

    fun <T> paginate(type: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): String

}
