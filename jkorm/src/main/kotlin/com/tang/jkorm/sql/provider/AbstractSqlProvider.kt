package com.tang.jkorm.sql.provider

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.AND
import com.tang.jkorm.constants.SqlString.ASC
import com.tang.jkorm.constants.SqlString.COMMA_SPACE
import com.tang.jkorm.constants.SqlString.DELETE_FROM
import com.tang.jkorm.constants.SqlString.DESC
import com.tang.jkorm.constants.SqlString.EQUAL
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
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.utils.Reflects.getColumnName
import com.tang.jkorm.utils.Reflects.getIdField
import com.tang.jkorm.utils.Reflects.getSqlFields
import com.tang.jkorm.utils.Reflects.getTableName
import com.tang.jkorm.utils.Reflects.isAutoIncrementId
import com.tang.jkorm.utils.Reflects.makeAccessible
import java.lang.reflect.Field

/**
 * Abstract SQL provider
 *
 * @author Tang
 */
abstract class AbstractSqlProvider : SqlProvider {

    abstract override fun providerType(): ProviderType

    override fun selectiveStrategy(any: Any?): Boolean {
        return JkOrmConfig.INSTANCE.selectiveStrategy.apply(any)
    }

    override fun appendColumns(sql: StringBuilder, fieldList: List<Field>) {
        fieldList.joinToString(COMMA_SPACE) { getColumnName(it) }.let { sql.append(it) }
    }

    override fun <T> appendWhere(sql: StringBuilder, parameters: MutableList<Any?>, clazz: Class<T>, entity: Any) {
        sql.append(WHERE)
        getSqlFields(clazz).filter {
            makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }.joinToString(AND) {
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL + QUESTION_MARK
        }.let { sql.append(it) }
    }

    override fun <T> appendOrderBy(sql: StringBuilder, orderBys: Array<OrderItem<T>>) {
        if (orderBys.isNotEmpty()) {
            sql.append(ORDER_BY)
            orderBys.joinToString(COMMA_SPACE) {
                it.column + if (it.asc) ASC else DESC
            }.let { sql.append(it) }
        }
    }

    override fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long) {
        sql.append(LIMIT).append(QUESTION_MARK).append(COMMA_SPACE).append(QUESTION_MARK)
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
    }

    override fun getSql(sql: StringBuilder): String {
        return JkOrmConfig.INSTANCE.getSql(sql)
    }

    override fun insert(entity: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = getIdField(clazz)
        val autoIncrementId = isAutoIncrementId(idField)
        val fieldList = getSqlFields(clazz).filter { getColumnName(it) != getColumnName(idField) || !autoIncrementId }
        sql.append(INSERT_INTO + getTableName(clazz) + SPACE + LEFT_BRACKET)
        appendColumns(sql, fieldList)
        sql.append(RIGHT_BRACKET + VALUES + LEFT_BRACKET)
        fieldList.joinToString {
            makeAccessible(it, entity)
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

    private fun getFieldValueMap(entity: Any): Map<Field, Any?> {
        return entity::class.java.declaredFields.associateWith { makeAccessible(it, entity); it.get(entity) }
    }

    override fun insertSelective(entity: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val fieldMap: Map<Field, Any?> = getFieldValueMap(entity)
        sql.append(INSERT_INTO + getTableName(clazz) + SPACE + LEFT_BRACKET)
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

    override fun batchInsert(entities: Iterable<Any>): SqlStatement {
        val sql = StringBuilder()
        val fieldNameList = arrayListOf<String>()
        val parameters = mutableListOf<Any?>()
        entities.first().let { entity ->
            val clazz = entity::class.java
            val idField = getIdField(clazz)
            val autoIncrementId = isAutoIncrementId(idField)
            val fieldList = getSqlFields(clazz).filter { getColumnName(it) != getColumnName(idField) || !autoIncrementId }
            sql.append(INSERT_INTO + getTableName(clazz) + SPACE + LEFT_BRACKET)
            appendColumns(sql, fieldList)
            sql.append(RIGHT_BRACKET + VALUES)
            fieldNameList.addAll(fieldList.map { getColumnName(it) })
        }
        entities.joinToString("$RIGHT_BRACKET$COMMA_SPACE$LEFT_BRACKET", LEFT_BRACKET, RIGHT_BRACKET) { entity ->
            val fieldList = entity::class.java.declaredFields.filter { getColumnName(it) in fieldNameList }
            fieldList.joinToString { field ->
                makeAccessible(field, entity)
                parameters.add(field.get(entity))
                QUESTION_MARK
            }
        }.let { sql.append(it) }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun batchInsertSelective(entities: Iterable<Any>): SqlStatement {
        val sql = StringBuilder()
        val fieldNameList = arrayListOf<String>()
        val parameters = mutableListOf<Any?>()
        entities.first().let { entity ->
            val clazz = entity::class.java
            val fieldMap: Map<Field, Any?> = getFieldValueMap(entity)
            sql.append(INSERT_INTO + getTableName(clazz) + SPACE + LEFT_BRACKET)
            fieldMap.filter { selectiveStrategy(it.value) }
                .map { getColumnName(it.key) }
                .joinToString(COMMA_SPACE)
                .let { sql.append(it) }
            fieldNameList.addAll(fieldMap.filter { selectiveStrategy(it.value) }.map { getColumnName(it.key) })
            sql.append(RIGHT_BRACKET + VALUES)
        }
        entities.joinToString("$RIGHT_BRACKET$COMMA_SPACE$LEFT_BRACKET", LEFT_BRACKET, RIGHT_BRACKET) { entity ->
            val fieldMap: Map<Field, Any?> = entity::class.java.declaredFields.associateWith { makeAccessible(it, entity); it.get(entity) }
            fieldMap.filter { selectiveStrategy(it.value) }
                .map {
                    parameters.add(it.value)
                    QUESTION_MARK
                }.joinToString(COMMA_SPACE)
        }.let { sql.append(it) }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun update(entity: Any): SqlStatement {
        return update(entity) { true }
    }

    override fun update(entity: Any, where: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val fieldList = getSqlFields(clazz).filter {
            makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }
        val whereFieldList = where::class.java.declaredFields.filter {
            makeAccessible(it, where)
            selectiveStrategy(it.get(where))
        }
        sql.append(UPDATE + getTableName(clazz) + SET)
        appendSetValues(sql, parameters, fieldList, entity)
        sql.append(WHERE)
        whereFieldList.joinToString(AND) {
            makeAccessible(it, where)
            parameters.add(it.get(where))
            getColumnName(it) + EQUAL + QUESTION_MARK
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
        val idField = getIdField(clazz)
        val fieldList = getSqlFields(clazz)
            .filter { getColumnName(it) != getColumnName(idField) }
            .filter {
                makeAccessible(it, entity)
                strategy(it.get(entity))
            }
        sql.append(UPDATE + getTableName(clazz) + SET)
        appendSetValues(sql, parameters, fieldList, entity)
        makeAccessible(idField, entity)
        sql.append(WHERE + getColumnName(idField) + EQUAL).append(QUESTION_MARK)
        parameters.add(idField.get(entity))
        return SqlStatement(getSql(sql), parameters)
    }

    private fun appendSetValues(sql: StringBuilder, parameters: MutableList<Any?>, fieldList: List<Field>, entity: Any) {
        fieldList.joinToString(COMMA_SPACE) {
            makeAccessible(it, entity)
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL + QUESTION_MARK
        }.let { sql.append(it) }
    }

    override fun <T> delete(clazz: Class<T>, entity: Any): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = getIdField(clazz)
        makeAccessible(idField, entity)
        sql.append(DELETE_FROM + getTableName(clazz) + WHERE)
        val fieldList = getSqlFields(clazz).filter {
            makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }
        fieldList.joinToString(AND) {
            makeAccessible(it, entity)
            parameters.add(it.get(entity))
            getColumnName(it) + EQUAL + QUESTION_MARK
        }.let { sql.append(it) }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> select(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT)
        appendColumns(sql, getSqlFields(clazz))
        sql.append(FROM + getTableName(clazz))
        entity?.let {
            appendWhere(sql, parameters, clazz, entity)
        }
        appendOrderBy(sql, orderBys)
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT_COUNT_FROM + getTableName(clazz))
        entity?.let {
            appendWhere(sql, parameters, clazz, entity)
        }
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT)
        appendColumns(sql, getSqlFields(clazz))
        sql.append(FROM + getTableName(clazz))
        if (entity != null) {
            appendWhere(sql, parameters, clazz, entity)
        }
        appendOrderBy(sql, orderBys)
        appendLimit(sql, parameters, pageNumber, pageSize)
        return SqlStatement(getSql(sql), parameters)
    }

}
