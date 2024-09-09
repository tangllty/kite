package com.tang.jkorm.sql.provider

import com.tang.jkorm.constants.SqlString.AND
import com.tang.jkorm.constants.SqlString.ASC
import com.tang.jkorm.constants.SqlString.COMMA_SPACE
import com.tang.jkorm.constants.SqlString.DELETE_FROM
import com.tang.jkorm.constants.SqlString.DESC
import com.tang.jkorm.constants.SqlString.EQUAL_BRACKET
import com.tang.jkorm.constants.SqlString.FROM
import com.tang.jkorm.constants.SqlString.INSERT_INTO
import com.tang.jkorm.constants.SqlString.LEFT_BRACKET
import com.tang.jkorm.constants.SqlString.LIMIT
import com.tang.jkorm.constants.SqlString.ORDER_BY
import com.tang.jkorm.constants.SqlString.QUESTION_MARK
import com.tang.jkorm.constants.SqlString.RIGHT_BRACKET
import com.tang.jkorm.constants.SqlString.SELECT
import com.tang.jkorm.constants.SqlString.SELECT_COUNT_FROM
import com.tang.jkorm.constants.SqlString.SET
import com.tang.jkorm.constants.SqlString.SPACE
import com.tang.jkorm.constants.SqlString.UPDATE
import com.tang.jkorm.constants.SqlString.VALUES
import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.utils.Reflects.getColumnName
import java.lang.reflect.Field

/**
 * Abstract SQL provider
 *
 * @author Tang
 */
abstract class AbstractSqlProvider : SqlProvider {

    override fun selectiveStrategy(any: Any?): Boolean {
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

    override fun appendColumns(sql: StringBuilder, fieldList: List<Field>) {
        fieldList.joinToString(COMMA_SPACE) { getColumnName(it) }.let { sql.append(it) }
    }

    override fun <T> appendWhere(sql: StringBuilder, parameters: MutableList<Any?>, clazz: Class<T>, entity: Any) {
        sql.append(WHERE)
        clazz.declaredFields.filter {
            Reflects.makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }.joinToString(AND) {
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL_BRACKET + QUESTION_MARK
        }.let { sql.append(it) }
    }

    override fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long) {
        sql.append(LIMIT).append(QUESTION_MARK).append(COMMA_SPACE).append(QUESTION_MARK)
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
    }

    override fun getSql(sql: StringBuilder): String {
        return sql.toString().lowercase()
    }

    override fun insert(entity: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = Reflects.getIdField(clazz)
        val autoIncrementId = Reflects.isAutoIncrementId(idField)
        val fieldList = clazz.declaredFields.filter { getColumnName(it) != getColumnName(idField) || !autoIncrementId }
        sql.append(INSERT_INTO + Reflects.getTableName(clazz) + SPACE + LEFT_BRACKET)
        appendColumns(sql, fieldList)
        sql.append(RIGHT_BRACKET + VALUES + LEFT_BRACKET)
        fieldList.joinToString {
            Reflects.makeAccessible(it, entity)
            if (it == idField && !autoIncrementId) {
                parameters.add(Reflects.getGeneratedId(idField))
            } else {
                parameters.add(it.get(entity))
            }
            QUESTION_MARK
        }.let { sql.append(it) }
        sql.append(RIGHT_BRACKET)
        return SqlStatement(getSql(sql), parameters)
    }

    override fun insertSelective(entity: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val fieldMap: Map<Field, Any?> = clazz.declaredFields.associateWith { Reflects.makeAccessible(it, entity); it.get(entity) }
        sql.append(INSERT_INTO + Reflects.getTableName(clazz) + SPACE + LEFT_BRACKET)
        fieldMap.filter { selectiveStrategy(it.value) }
            .map { getColumnName(it.key) }
            .joinToString(COMMA_SPACE)
            .let { sql.append(it) }
        sql.append(RIGHT_BRACKET + VALUES + LEFT_BRACKET)
        fieldMap.filter { selectiveStrategy(it.value) }
            .map {
                parameters.add(it.value)
                QUESTION_MARK
            }.joinToString(COMMA_SPACE)
            .let { sql.append(it) }
        sql.append(RIGHT_BRACKET)
        return SqlStatement(getSql(sql), parameters)
    }

    override fun update(entity: Any): SqlStatement {
        return update(entity) { true }
    }

    override fun update(entity: Any, where: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val fieldList = clazz.declaredFields.filter {
            Reflects.makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }
        val whereFieldList = where::class.java.declaredFields.filter {
            Reflects.makeAccessible(it, where)
            selectiveStrategy(it.get(where))
        }
        sql.append(UPDATE + Reflects.getTableName(clazz) + SET)
        appendSetValues(sql, parameters, fieldList, entity)
        sql.append(WHERE)
        whereFieldList.joinToString(AND) {
            Reflects.makeAccessible(it, where)
            parameters.add(it.get(where))
            getColumnName(it) + EQUAL_BRACKET + QUESTION_MARK
        }.let { sql.append(it) }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun updateSelective(entity: Any): SqlStatement {
        return update(entity) { selectiveStrategy(it) }
    }

    private fun update(entity: Any, strategy: (Any?) -> Boolean): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = Reflects.getIdField(clazz)
        val fieldList = clazz.declaredFields
            .filter { getColumnName(it) != getColumnName(idField) }
            .filter {
                Reflects.makeAccessible(it, entity)
                strategy(it.get(entity))
            }
        sql.append(UPDATE + Reflects.getTableName(clazz) + SET)
        appendSetValues(sql, parameters, fieldList, entity)
        Reflects.makeAccessible(idField, entity)
        sql.append(WHERE + getColumnName(idField) + EQUAL_BRACKET).append(QUESTION_MARK)
        parameters.add(idField.get(entity))
        return SqlStatement(getSql(sql), parameters)
    }

    private fun appendSetValues(sql: StringBuilder, parameters: MutableList<Any?>, fieldList: List<Field>, entity: Any) {
        fieldList.joinToString(COMMA_SPACE) {
            Reflects.makeAccessible(it, entity)
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL_BRACKET + QUESTION_MARK
        }.let { sql.append(it) }
    }

    override fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = Reflects.getIdField(clazz)
        Reflects.makeAccessible(idField, entity)
        sql.append(DELETE_FROM + Reflects.getTableName(clazz) + WHERE)
        val fieldList = clazz.declaredFields.filter {
            Reflects.makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }
        fieldList.joinToString(AND) {
            Reflects.makeAccessible(it, entity)
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL_BRACKET + QUESTION_MARK
        }.let { sql.append(it) }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> select(clazz: Class<T>, entity: Any?): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT)
        appendColumns(sql, listOf(*clazz.declaredFields))
        sql.append(SPACE + FROM + Reflects.getTableName(clazz))
        entity?.let {
            appendWhere(sql, parameters, clazz, entity)
        }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT_COUNT_FROM + Reflects.getTableName(clazz))
        entity?.let {
            appendWhere(sql, parameters, clazz, entity)
        }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<Pair<String, Boolean>>, pageNumber: Long, pageSize: Long): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT)
        appendColumns(sql, listOf(*clazz.declaredFields))
        sql.append(SPACE + FROM + Reflects.getTableName(clazz))
        if (entity != null) {
            appendWhere(sql, parameters, clazz, entity)
        }
        if (orderBys.isNotEmpty()) {
            sql.append(SPACE + ORDER_BY)
            orderBys.joinToString(COMMA_SPACE) {
                it.first + if (it.second) ASC else DESC
            }.let { sql.append(it) }
        }
        appendLimit(sql, parameters, pageNumber, pageSize)
        return SqlStatement(getSql(sql), parameters)
    }

}
