package com.tang.kite.sql

import com.tang.kite.constants.SqlString.AND
import com.tang.kite.constants.SqlString.ASC
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.DELETE_FROM
import com.tang.kite.constants.SqlString.DESC
import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.constants.SqlString.EQUAL
import com.tang.kite.constants.SqlString.FROM
import com.tang.kite.constants.SqlString.IN
import com.tang.kite.constants.SqlString.INSERT_INTO
import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.LEFT_JOIN
import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.ON
import com.tang.kite.constants.SqlString.ORDER_BY
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.constants.SqlString.SELECT
import com.tang.kite.constants.SqlString.SELECT_COUNT_FROM
import com.tang.kite.constants.SqlString.SELECT_DISTINCT
import com.tang.kite.constants.SqlString.SET
import com.tang.kite.constants.SqlString.SPACE
import com.tang.kite.constants.SqlString.UPDATE
import com.tang.kite.constants.SqlString.VALUES
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.utils.Reflects.getTableName

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

        TODO()
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
