package com.tang.kite.sql.ast.dql

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString
import com.tang.kite.enumeration.SqlType
import com.tang.kite.sql.ast.AbstractSqlHandler
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.SqlStatement

/**
 * @author Tang
 */
object SqlStatementDqlHandler : AbstractSqlHandler(), DqlHandler<SqlStatement> {

    override fun handleSelect(selectNode: SqlNode.Select, dialect: SqlDialect): SqlStatement {
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
            dialect.applyLimitClause(sql, parameters, limit)
        }

        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

}
