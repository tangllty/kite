package com.tang.kite.sql.ast.dml

import com.tang.kite.config.SqlConfig
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.constants.SqlString
import com.tang.kite.enumeration.SqlType
import com.tang.kite.sql.Column
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects

/**
 * @author Tang
 */
object SqlStatementHandler : AbstractDmlHandler<SqlStatement>() {

    override fun handleSelect(selectNode: SqlNode.Select, dialect: SqlDialect?): SqlStatement {
        val (columns, distinct, count, from, joins, where, groupBy, having, orderBy, limit) = selectNode
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val withAlias = joins.isNotEmpty()

        if (count) {
            sql.append(if (distinct) SqlString.SELECT_DISTINCT_COUNT_FROM else SqlString.SELECT_COUNT_FROM)
        } else {
            appendColumns(columns, from, joins)
            sql.append(if (distinct) SqlString.SELECT_DISTINCT else SqlString.SELECT)
            sql.append(columns.joinToString(SqlString.COMMA_SPACE) { it.toString(withAlias) })
            sql.append(SqlString.FROM)
        }

        appendTable(sql, from, withAlias)
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, from, parameters, where, SqlType.SELECT, withAlias)

        if (groupBy.isNotEmpty()) {
            sql.append(SqlString.GROUP_BY)
            val orderBys = groupBy.joinToString(SqlString.COMMA_SPACE) { it.toString(withAlias) }
            sql.append(orderBys)
        }

        if (having.isNotEmpty()) {
            sql.append(SqlString.HAVING)
            if (having.last().nestedConditions.isEmpty()) {
                having.last().logicalOperator = null
            }
            having.forEach {
                it.appendSql(sql, parameters, withAlias)
            }
        }

        if (orderBy.isNotEmpty()) {
            sql.append(SqlString.ORDER_BY)
            orderBy.joinToString(SqlString.COMMA_SPACE) {
                it.column.toString(withAlias) + if (it.asc) SqlString.ASC else SqlString.DESC
            }.let {
                sql.append(it)
            }
        }

        if (limit != null) {
            if (dialect == null) {
                throw IllegalArgumentException("When limit clause is used, dialect is required")
            }
            dialect.applyLimitClause(sql, parameters, limit)
        }

        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    override fun handleInsert(insertNode: SqlNode.Insert): SqlStatement {
        val (table, columns, valuesList) = insertNode
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        applyInsertLogical(insertNode)
        applyInsertTenant(insertNode)
        appendInsertPrefix(sql, table, columns)
        valuesList.joinToString("${SqlString.RIGHT_BRACKET}${SqlString.COMMA_SPACE}${SqlString.LEFT_BRACKET}",
            SqlString.LEFT_BRACKET,
            SqlString.RIGHT_BRACKET
        ) { values ->
            values.joinToString(SqlString.COMMA_SPACE) {
                parameters.add(it)
                SqlString.QUESTION_MARK
            }
        }.let { sql.append(it) }
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    override fun handleUpdate(updateNode: SqlNode.Update): SqlStatement {
        val (table, sets, _, joins, where) = updateNode
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val withAlias = joins.isNotEmpty()
        sql.append(SqlString.UPDATE)
        appendTable(sql, table)
        sql.append(SqlString.SET)
        sql.append(sets.entries.joinToString(SqlString.COMMA_SPACE) {
            parameters.add(it.value)
            "${it.key.toString(withAlias)} = ${SqlString.QUESTION_MARK}"
        })
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, table, parameters, where, SqlType.UPDATE, withAlias)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    override fun handleDelete(deleteNode: SqlNode.Delete): SqlStatement {
        val (table, joins, where) = deleteNode
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        if (shouldApplyLogicalDeletion(table)) {
            val update = SqlNode.Update()
            update.table = table
            val logicalField = Reflects.getLogicalField(table?.clazz!!)
            val logicalValue = LogicalDeletionConfig.logicalDeletionProcessor.process(logicalField)
            update.sets[Column(logicalField)] = logicalValue.deletedValue
            update.where.addAll(where)
            return handleUpdate(update)
        }
        sql.append(SqlString.DELETE_FROM)
        appendTable(sql, table)
        val withAlias = joins.isNotEmpty()
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, parameters, where, withAlias)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

}
