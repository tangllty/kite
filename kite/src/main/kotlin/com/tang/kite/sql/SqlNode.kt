package com.tang.kite.sql

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.ASC
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.DESC
import com.tang.kite.constants.SqlString.FROM
import com.tang.kite.constants.SqlString.GROUP_BY
import com.tang.kite.constants.SqlString.HAVING
import com.tang.kite.constants.SqlString.ORDER_BY
import com.tang.kite.constants.SqlString.SELECT
import com.tang.kite.constants.SqlString.SELECT_DISTINCT
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement

/**
 * @author Tang
 */
sealed class SqlNode {

    data class Select(
        val columns: MutableList<Column> = mutableListOf(),
        var distinct: Boolean = false,
        var from: TableReference? = null,
        val joins: MutableList<JoinTable> = mutableListOf(),
        val where: MutableList<LogicalStatement> = mutableListOf(),
        val groupBy: MutableList<Column> = mutableListOf(),
        val having: MutableList<LogicalStatement> = mutableListOf(),
        val orderBy: MutableList<OrderItem<*>> = mutableListOf(),
        val limit: LimitClause? = null
    ) : SqlNode()

    data class Insert(
        val table: TableReference? = null,
        val columns: MutableList<Column> = mutableListOf(),
        val values: MutableList<Any?> = mutableListOf()
    ) : SqlNode()

    data class Update(
        val table: TableReference? = null,
        val sets: LinkedHashMap<String, Any?> = linkedMapOf(),
        val joins: MutableList<JoinTable> = mutableListOf(),
        val where: MutableList<LogicalStatement> = mutableListOf()
    ) : SqlNode()

    data class Delete(
        val table: TableReference? = null,
        val joins: MutableList<JoinTable> = mutableListOf(),
        val where: MutableList<LogicalStatement> = mutableListOf()
    ) : SqlNode()

    fun getSqlStatement(dialect: SqlDialect): SqlStatement {
        return when (this) {
            is Select -> getSelectSqlStatement(dialect, this)
            is Insert -> getInsertSqlStatement(dialect, this)
            is Update -> getUpdateSqlStatement(dialect, this)
            is Delete -> getDeleteSqlStatement(dialect, this)
        }
    }

    fun getSql(dialect: SqlDialect): String {
        return getSqlStatement(dialect).getActualSql()
    }

    private fun getSelectSqlStatement(dialect: SqlDialect, select: Select): SqlStatement {
        val (columns, distinct, from, joins, where, groupBy, having, orderBy, limit) = select
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val withAlias = joins.isNotEmpty()
        sql.append(if (distinct) SELECT_DISTINCT else SELECT)

        sql.append(columns.joinToString(COMMA_SPACE) { it.toString(withAlias) })

        sql.append(FROM + (from?.toString(withAlias) ?: throw IllegalArgumentException("Table reference can not be null")))

        if (joins.isNotEmpty()) {
            joins.forEach { sql.append(it.toString(withAlias)) }
        }

        if (where.isNotEmpty()) {
            sql.append(WHERE)
            if (where.last().nestedConditions.isEmpty()) {
                where.last().logicalOperator = null
            }
            where.forEach {
                it.appendSql(sql, parameters, withAlias)
            }
        }

        if (groupBy.isNotEmpty()) {
            sql.append(GROUP_BY)
            val orderBys = groupBy.joinToString(COMMA_SPACE) { it.toString(withAlias) }
            sql.append(orderBys)
        }

        if (having.isNotEmpty()) {
            sql.append(HAVING)
            if (having.last().nestedConditions.isEmpty()) {
                having.last().logicalOperator = null
            }
            having.forEach {
                it.appendSql(sql, parameters, withAlias)
            }
        }

        if (orderBy.isNotEmpty()) {
            sql.append(ORDER_BY)
            orderBy.joinToString(COMMA_SPACE) {
                it.column.toString(withAlias) + if (it.asc) ASC else DESC
            }.let {
                sql.append(it)
            }
        }

        if (limit != null) {
            dialect.applyLimitClause(sql, parameters, limit)
        }

        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    private fun getInsertSqlStatement(dialect: SqlDialect, insert: Insert): SqlStatement {
        TODO()
    }

    private fun getUpdateSqlStatement(dialect: SqlDialect, update: Update): SqlStatement {
        TODO()
    }

    private fun getDeleteSqlStatement(dialect: SqlDialect, delete: Delete): SqlStatement {
        TODO()
    }

}
