package com.tang.kite.sql

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.ASC
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.DELETE_FROM
import com.tang.kite.constants.SqlString.DESC
import com.tang.kite.constants.SqlString.FROM
import com.tang.kite.constants.SqlString.GROUP_BY
import com.tang.kite.constants.SqlString.HAVING
import com.tang.kite.constants.SqlString.INSERT_INTO
import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.ORDER_BY
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.constants.SqlString.SELECT
import com.tang.kite.constants.SqlString.SELECT_DISTINCT
import com.tang.kite.constants.SqlString.SET
import com.tang.kite.constants.SqlString.SPACE
import com.tang.kite.constants.SqlString.UPDATE
import com.tang.kite.constants.SqlString.VALUES
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.BatchSqlStatement
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

        var table: TableReference? = null,

        val columns: MutableList<Column> = mutableListOf(),

        val valuesList: MutableList<MutableList<Any?>> = mutableListOf(),

        val columnsValuesList: MutableList<Pair<MutableList<Column>, MutableList<Any?>>> = mutableListOf()

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

    fun getSqlStatement(dialect: SqlDialect? = null): SqlStatement {
        return when (this) {
            is Select -> {
                if (dialect == null) {
                    throw IllegalArgumentException("distinct can not be null")
                }
                getSelectSqlStatement(this, dialect)
            }
            is Insert -> getInsertSqlStatement(this)
            is Update -> getUpdateSqlStatement(this)
            is Delete -> getDeleteSqlStatement(this)
        }
    }

    fun getBatchSqlStatement(): BatchSqlStatement {
        return when (this) {
            is Insert -> getInsertBatchSqlStatement(this)
            else -> throw IllegalArgumentException("only insert statement can be batch")
        }
    }

    fun getInsertSqlStatementList(): List<SqlStatement> {
        return when (this) {
            is Insert -> getInsertSqlStatementList(this)
            else -> throw IllegalArgumentException("only insert statement can be batch")
        }
    }

    fun getSql(dialect: SqlDialect? = null): String {
        return getSqlStatement(dialect).getActualSql()
    }

    private fun getSelectSqlStatement(select: Select, dialect: SqlDialect): SqlStatement {
        val (columns, distinct, from, joins, where, groupBy, having, orderBy, limit) = select
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        val withAlias = joins.isNotEmpty()
        sql.append(if (distinct) SELECT_DISTINCT else SELECT)

        sql.append(columns.joinToString(COMMA_SPACE) { it.toString(withAlias) })
        sql.append(FROM)
        appendTable(sql, from, withAlias)
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, parameters, where, withAlias)

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

    private fun appendInsertPrefix(sql: StringBuilder, table: TableReference?, columns: MutableList<Column>) {
        sql.append(INSERT_INTO)
        appendTable(sql, table)
        sql.append(SPACE + LEFT_BRACKET)
        sql.append(columns.joinToString(COMMA_SPACE) { it.toString() })
        sql.append(RIGHT_BRACKET + VALUES)
    }

    private fun getInsertSqlStatement(insert: Insert): SqlStatement {
        val (table, columns, valuesList) = insert
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        appendInsertPrefix(sql, table, columns)
        valuesList.joinToString("$RIGHT_BRACKET$COMMA_SPACE$LEFT_BRACKET", LEFT_BRACKET, RIGHT_BRACKET) { values ->
            values.joinToString(COMMA_SPACE) {
                parameters.add(it)
                QUESTION_MARK
            }
        }.let { sql.append(it) }
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    private fun getInsertBatchSqlStatement(insert: Insert): BatchSqlStatement {
        val (table, columns, valuesList) = insert
        val sql = StringBuilder()
        appendInsertPrefix(sql, table, columns)
        sql.append(LEFT_BRACKET)
        sql.append(valuesList.first().joinToString(COMMA_SPACE) { QUESTION_MARK })
        sql.append(RIGHT_BRACKET)
        return BatchSqlStatement(SqlConfig.getSql(sql), insert.valuesList)
    }

    private fun getInsertSqlStatementList(insert: Insert): List<SqlStatement> {
        val (table, _, _, columnsValuesList) = insert
        return columnsValuesList.map { (columns, values) ->
            val sql = StringBuilder()
            val parameters = mutableListOf<Any?>()
            appendInsertPrefix(sql, table, columns)
            sql.append(LEFT_BRACKET)
            sql.append(values.joinToString(COMMA_SPACE) {
                parameters.add(it)
                QUESTION_MARK
            })
            sql.append(RIGHT_BRACKET)
            SqlStatement(SqlConfig.getSql(sql), parameters)
        }
    }

    private fun getUpdateSqlStatement(update: Update): SqlStatement {
        val (table, sets, joins, where) = update
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(UPDATE)
        appendTable(sql, table)
        sql.append(SPACE + SET)
        sql.append(sets.entries.joinToString(COMMA_SPACE) {
            parameters.add(it.value)
            "${it.key} = $QUESTION_MARK"
        })
        val withAlias = joins.isNotEmpty()
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, parameters, where, withAlias)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    private fun getDeleteSqlStatement(delete: Delete): SqlStatement {
        val (table, joins, where) = delete
        val sql = StringBuilder()
        val parameters = mutableListOf<Any?>()
        sql.append(DELETE_FROM)
        appendTable(sql, table)
        val withAlias = joins.isNotEmpty()
        appendJoins(joins, sql, withAlias)
        appendWhere(sql, parameters, where, withAlias)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    private fun appendTable(sql: StringBuilder, from: TableReference?, withAlias: Boolean = false) {
        sql.append(from?.toString(withAlias) ?: throw IllegalArgumentException("Table reference can not be null"))
    }

    private fun appendJoins(joins: MutableList<JoinTable>, sql: StringBuilder, withAlias: Boolean) {
        if (joins.isNotEmpty()) {
            joins.forEach { sql.append(it.toString(withAlias)) }
        }
    }

    private fun appendWhere(sql: StringBuilder, parameters: MutableList<Any?>, where: MutableList<LogicalStatement>, withAlias: Boolean) {
        if (where.isNotEmpty()) {
            sql.append(WHERE)
            if (where.last().nestedConditions.isEmpty()) {
                where.last().logicalOperator = null
            }
            where.forEach {
                it.appendSql(sql, parameters, withAlias)
            }
        }
    }

}
