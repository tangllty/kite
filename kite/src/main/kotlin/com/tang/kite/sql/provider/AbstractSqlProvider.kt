package com.tang.kite.sql.provider

import com.tang.kite.annotation.Join
import com.tang.kite.config.KiteConfig
import com.tang.kite.constants.SqlString
import com.tang.kite.constants.SqlString.AND
import com.tang.kite.constants.SqlString.ASC
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.DELETE_FROM
import com.tang.kite.constants.SqlString.DESC
import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.constants.SqlString.EQUAL
import com.tang.kite.constants.SqlString.FROM
import com.tang.kite.constants.SqlString.INSERT_INTO
import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.ORDER_BY
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.constants.SqlString.SELECT
import com.tang.kite.constants.SqlString.SELECT_COUNT_FROM
import com.tang.kite.constants.SqlString.SET
import com.tang.kite.constants.SqlString.SPACE
import com.tang.kite.constants.SqlString.UPDATE
import com.tang.kite.constants.SqlString.VALUES
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.SqlStatement
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getColumnName
import com.tang.kite.utils.Reflects.getIdField
import com.tang.kite.utils.Reflects.getSqlFields
import com.tang.kite.utils.Reflects.getTableAlias
import com.tang.kite.utils.Reflects.getTableName
import com.tang.kite.utils.Reflects.isAutoIncrementId
import com.tang.kite.utils.Reflects.makeAccessible
import java.lang.reflect.Field

/**
 * Abstract SQL provider
 *
 * @author Tang
 */
abstract class AbstractSqlProvider : SqlProvider {

    abstract override fun providerType(): ProviderType

    override fun selectiveStrategy(any: Any?): Boolean {
        return KiteConfig.selectiveStrategy.apply(any)
    }

    override fun getSql(sql: StringBuilder): String {
        return KiteConfig.getSql(sql)
    }

    override fun getInCondition(sql: String, field: String, values: Iterable<Any?>, withAlias: Boolean): SqlStatement {
        val sqlBuilder = StringBuilder(sql)
        if (sqlBuilder.contentEquals(WHERE)) {
            sqlBuilder.append(AND)
        } else {
            sqlBuilder.append(WHERE)
        }
        sqlBuilder.append(field, SqlString.IN, LEFT_BRACKET)
        values.joinToString(COMMA_SPACE) {
            QUESTION_MARK
        }.let { sqlBuilder.append(it) }
        sqlBuilder.append(RIGHT_BRACKET)
        return SqlStatement(getSql(sqlBuilder), values.toMutableList())
    }

    override fun getNestedSelect(sql: String, field: String, value: Iterable<Any?>, join: Join): SqlStatement {
        val sqlBuilder = StringBuilder(sql)
        if (sqlBuilder.contentEquals(WHERE)) {
            sqlBuilder.append(AND)
        } else {
            sqlBuilder.append(WHERE)
        }
        sqlBuilder.append(field, SqlString.IN, LEFT_BRACKET, SPACE)
        sqlBuilder.append(SELECT, join.joinTargetColumn, FROM, join.joinTable)
        sqlBuilder.append(WHERE, join.joinSelfColumn, EQUAL, QUESTION_MARK)
        sqlBuilder.append(SPACE, RIGHT_BRACKET)
        return SqlStatement(getSql(sqlBuilder), value.toMutableList())
    }

    override fun getColumns(fieldList: List<Field>, withAlias: Boolean): String {
        return fieldList.joinToString(COMMA_SPACE) { getColumnName(it, withAlias) }
    }

    override fun <T> getWhere(parameters: MutableList<Any?>, clazz: Class<T>, entity: Any?, withAlias: Boolean): String {
        if (entity == null) {
            return ""
        }
        val whereSql = getSqlFields(clazz).filter {
            makeAccessible(it, entity)
            selectiveStrategy(it.get(entity))
        }.joinToString(AND) {
            parameters.add(it.get(entity))
            getColumnName(it, withAlias) + EQUAL + QUESTION_MARK
        }
        return "$WHERE$whereSql"
    }

    override fun <T> getOrderBy(orderBys: Array<OrderItem<T>>, withAlias: Boolean): String {
        if (orderBys.isEmpty()) {
            return ""
        }
        val orderBysSql = orderBys.joinToString(COMMA_SPACE) {
            it.column.toString(withAlias) + if (it.asc) ASC else DESC
        }
        return "$ORDER_BY$orderBysSql"
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add(pageSize)
        parameters.add((pageNumber - 1) * pageSize)
        return "$LIMIT$QUESTION_MARK$OFFSET$QUESTION_MARK"
    }

    override fun insert(entity: Any): SqlStatement {
        val clazz = entity::class.java
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val idField = getIdField(clazz)
        val autoIncrementId = isAutoIncrementId(idField)
        val fieldList = getSqlFields(clazz).filter { getColumnName(it) != getColumnName(idField) || !autoIncrementId }
        sql.append(INSERT_INTO + getTableName(clazz) + SPACE + LEFT_BRACKET)
        sql.append(getColumns(fieldList))
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
            sql.append(getColumns(fieldList))
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
        sql.append(getColumns(getSqlFields(clazz)))
        sql.append(FROM + getTableName(clazz))
        sql.append(getWhere(parameters, clazz, entity))
        sql.append(getOrderBy(orderBys))
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> selectWithJoins(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, withAlias: Boolean): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val tableName = getTableName(clazz)
        val tableAlias = getTableAlias(clazz)
        val joins = Reflects.getJoins(clazz)
        val addAlias = withAlias && joins.isNotEmpty()
        sql.append(SELECT)
        val sqlFields = getSqlFields(clazz).toMutableList()
        joins.forEach { sqlFields.addAll(getSqlFields(it.type)) }
        sql.append(getColumns(sqlFields, addAlias))
        sql.append(FROM, tableName)
        if (addAlias) {
            sql.append(SPACE, tableAlias)
        }
        joins.forEach {
            val joinTableAlias = getTableAlias(it.type)
            val join = it.getAnnotation(Join::class.java)
            val selfField = join.selfField
            val targetField = join.targetField
            val joinTable = join.joinTable
            val joinSelfField = join.joinSelfColumn
            val joinTargetField = join.joinTargetColumn
            if (joinTable.isNotEmpty() && joinSelfField.isNotEmpty() && joinTargetField.isNotEmpty()) {
                val innerJoinTableAlias = joinTable.split("_").joinToString("") { it.first().toString() }
                sql.append(SqlString.LEFT_JOIN, joinTable, SPACE, innerJoinTableAlias, SqlString.ON)
                sql.append(innerJoinTableAlias, DOT, joinSelfField, EQUAL, tableAlias, DOT, selfField)
                sql.append(SqlString.LEFT_JOIN, getTableName(it.type), SPACE, joinTableAlias, SqlString.ON)
                sql.append(joinTableAlias, DOT, targetField, EQUAL, innerJoinTableAlias, DOT, joinTargetField)
            } else {
                sql.append(SqlString.LEFT_JOIN, getTableName(it.type), SPACE, joinTableAlias, SqlString.ON)
                sql.append(tableAlias, DOT, selfField, EQUAL, joinTableAlias, DOT, targetField)
            }
        }
        sql.append(getWhere(parameters, clazz, entity, addAlias))
        sql.append(getOrderBy(orderBys, addAlias))
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> count(clazz: Class<T>, entity: Any?): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT_COUNT_FROM + getTableName(clazz))
        sql.append(getWhere(parameters, clazz, entity))
        return SqlStatement(getSql(sql), parameters)
    }

    override fun <T> paginate(clazz: Class<T>, entity: Any?, orderBys: Array<OrderItem<T>>, pageNumber: Long, pageSize: Long): SqlStatement {
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(SELECT)
        sql.append(getColumns(getSqlFields(clazz)))
        sql.append(FROM + getTableName(clazz))
        sql.append(getWhere(parameters, clazz, entity))
        sql.append(getOrderBy(orderBys))
        sql.append(getLimit(parameters, pageNumber, pageSize))
        return SqlStatement(getSql(sql), parameters)
    }

}
