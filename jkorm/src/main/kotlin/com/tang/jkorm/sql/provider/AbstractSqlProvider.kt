package com.tang.jkorm.sql.provider

import com.tang.jkorm.constants.SqlString.AND_BRACKET
import com.tang.jkorm.constants.SqlString.ASC
import com.tang.jkorm.constants.SqlString.COMMA
import com.tang.jkorm.constants.SqlString.DELETE_FROM
import com.tang.jkorm.constants.SqlString.DESC
import com.tang.jkorm.constants.SqlString.EQUAL
import com.tang.jkorm.constants.SqlString.EQUAL_BRACKET
import com.tang.jkorm.constants.SqlString.INSERT_INTO
import com.tang.jkorm.constants.SqlString.LEFT_BRACKET
import com.tang.jkorm.constants.SqlString.LIMIT
import com.tang.jkorm.constants.SqlString.ORDER_BY
import com.tang.jkorm.constants.SqlString.RIGHT_BRACKET
import com.tang.jkorm.constants.SqlString.SELECT_ALL_FROM
import com.tang.jkorm.constants.SqlString.SELECT_COUNT_FROM
import com.tang.jkorm.constants.SqlString.SET
import com.tang.jkorm.constants.SqlString.SINGLE_QUOTE
import com.tang.jkorm.constants.SqlString.SPACE
import com.tang.jkorm.constants.SqlString.UPDATE
import com.tang.jkorm.constants.SqlString.VALUES
import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.utils.Reflects
import java.lang.reflect.Field

/**
 * Abstract SQL provider
 *
 * @author Tang
 */
abstract class AbstractSqlProvider : SqlProvider {

    fun selectiveStrategy(any: Any?): Boolean {
        if (any == null) {
            return false
        }
        if (any is String) {
            return any.isNotEmpty()
        }
        if (any is Int) {
            return any != 0
        }
        return true
    }

    fun appendColumns(sql: StringBuilder, fieldList: List<Field>) {
        fieldList.joinToString(COMMA + SPACE) { it.name }
            .let { sql.append(it) }
    }

    fun appendValues(sql: StringBuilder, fieldList: List<Field>, entity: Any) {
        fieldList.map {
            Reflects.makeAccessible(it, entity)
            if (it.type == String::class.java) {
                SINGLE_QUOTE + it.get(entity) + SINGLE_QUOTE
            } else {
                it.get(entity)
            }
        }.joinToString(COMMA + SPACE)
            .let { sql.append(it) }
    }

    fun appendSetValues(sql: StringBuilder, fieldList: List<Field>, entity: Any) {
        fieldList.joinToString(COMMA + SPACE) {
            Reflects.makeAccessible(it, entity)
            getEqual(it.name, it.get(entity))
        }.let { sql.append(it) }
    }

    fun getValue(value: Any): String {
        return if (value is String) {
            SINGLE_QUOTE + value + SINGLE_QUOTE
        } else {
            value.toString()
        }
    }

    fun getEqual(field: String, value: Any): String {
        return if (value is String) {
            field + EQUAL_BRACKET + SINGLE_QUOTE + value + SINGLE_QUOTE
        } else {
            field + EQUAL_BRACKET + value
        }
    }

    fun appendValue(sql: StringBuilder, field: String, value: Any) {
        if (value is String) {
            sql.append(field).append(EQUAL + SINGLE_QUOTE).append(value).append(SINGLE_QUOTE + COMMA)
        } else {
            sql.append(field).append(EQUAL).append(value).append(COMMA)
        }
    }

    override fun getSql(sql: StringBuilder): String {
        return sql.toString().lowercase()
    }

    override fun insert(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        val autoIncrementId = Reflects.isAutoIncrementId(clazz)
        val fieldList = clazz.declaredFields.filter { it.name != idField.name || !autoIncrementId }
        sql.append(INSERT_INTO + Reflects.getTableName(clazz) + SPACE + LEFT_BRACKET)
        appendColumns(sql, fieldList)
        sql.append(RIGHT_BRACKET + VALUES + LEFT_BRACKET)
        appendValues(sql, fieldList, entity)
        sql.append(RIGHT_BRACKET)
        return getSql(sql)
    }

    override fun insertSelective(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val fieldMap: Map<Field, Any?> = clazz.declaredFields.associateWith { Reflects.makeAccessible(it, entity); it.get(entity) }
        sql.append(INSERT_INTO + Reflects.getTableName(clazz) + SPACE + LEFT_BRACKET)
        fieldMap.filter { selectiveStrategy(it.value) }
            .map { it.key.name }
            .joinToString(COMMA + SPACE)
            .let { sql.append(it) }
        sql.append(RIGHT_BRACKET + VALUES + LEFT_BRACKET)
        fieldMap.filter { selectiveStrategy(it.value) }
            .map { getValue(it.value!!) }
            .joinToString(COMMA + SPACE)
            .let { sql.append(it) }
        sql.append(RIGHT_BRACKET)
        return getSql(sql)
    }

    override fun update(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        val fieldList = clazz.declaredFields.filter { it.name != idField.name }
        sql.append(UPDATE + Reflects.getTableName(clazz) + SET)
        appendSetValues(sql, fieldList, entity)
        Reflects.makeAccessible(idField, entity)
        sql.append(SPACE + WHERE + idField.name + EQUAL_BRACKET).append(idField.get(entity))
        return getSql(sql)
    }

    override fun updateSelective(entity: Any): String {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        val fieldList = clazz.declaredFields
            .filter { it.name != idField.name }
            .filter {
                Reflects.makeAccessible(it, entity)
                selectiveStrategy(it.get(entity))
            }
        sql.append(UPDATE + Reflects.getTableName(clazz) + SET)
        appendSetValues(sql, fieldList, entity)
        Reflects.makeAccessible(idField, entity)
        sql.append(SPACE + WHERE + idField.name + EQUAL_BRACKET).append(idField.get(entity))
        return getSql(sql)
    }

    override fun <T> delete(clazz: Class<T>, entity: Any): String {
        val sql = StringBuilder()
        val idField = Reflects.getIdField(clazz)
        Reflects.makeAccessible(idField, entity)
        sql.append(DELETE_FROM + Reflects.getTableName(clazz) + SPACE + WHERE)
            .append(idField.name).append(EQUAL_BRACKET).append(idField.get(entity))
        return getSql(sql)
    }

    override fun <T> select(clazz: Class<T>, entity: Any?): String {
        val sql = StringBuilder()
        sql.append(SELECT_ALL_FROM + Reflects.getTableName(clazz))
        if (entity == null) {
            return getSql(sql)
        }
        sql.append(SPACE + WHERE)
        clazz.declaredFields.filter {
            Reflects.makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }.joinToString(AND_BRACKET) {
            getEqual(it.name, it.get(entity))
        }.let { sql.append(it) }
        return getSql(sql)
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): String {
        val sql = StringBuilder()
        sql.append(SELECT_COUNT_FROM + Reflects.getTableName(clazz))
        if (entity == null) {
            return getSql(sql)
        }
        sql.append(SPACE + WHERE)
        clazz.declaredFields.filter {
            Reflects.makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }.joinToString(AND_BRACKET) {
            getEqual(it.name, it.get(entity))
        }.let { sql.append(it) }
        return getSql(sql)
    }

    override fun <T> paginate(type: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): String {
        val sql = StringBuilder()
        sql.append(SELECT_ALL_FROM + Reflects.getTableName(type))
        if (entity != null) {
            sql.append(SPACE + WHERE)
            type.declaredFields
                .filter {
                    Reflects.makeAccessible(it, entity)
                    selectiveStrategy(it.get(entity))
                }.joinToString(AND_BRACKET) {
                    getEqual(it.name, it.get(entity))
                }.let { sql.append(it) }
        }
        if (orderBys.isNotEmpty()) {
            sql.append(SPACE + ORDER_BY)
            orderBys.joinToString(COMMA + SPACE) {
                it.first + if (it.second) ASC else DESC
            }.let { sql.append(it) }
        }
        sql.append(LIMIT).append((pageNumber - 1) * pageSize).append(COMMA).append(pageSize)
        return getSql(sql)
    }

}
