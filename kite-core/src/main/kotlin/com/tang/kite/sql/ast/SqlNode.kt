package com.tang.kite.sql.ast

import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.dml.BatchSqlStatementHandler
import com.tang.kite.sql.ast.dml.SqlStatementHandler
import com.tang.kite.sql.ast.dml.DmlHandler
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement

/**
 * @author Tang
 */
sealed class SqlNode {

    sealed class DmlNode : SqlNode() {

        fun <T> accept(visitor: DmlHandler<T>, dialect: SqlDialect?): T {
            return when (this) {
                is Select -> visitor.handleSelect(this, dialect)
                is Insert -> visitor.handleInsert(this)
                is Update -> visitor.handleUpdate(this)
                is Delete -> visitor.handleDelete(this)
            }
        }

        fun getSqlStatement(dialect: SqlDialect? = null): SqlStatement {
            return accept(SqlStatementHandler, dialect)
        }

        fun getBatchSqlStatement(): BatchSqlStatement {
            return accept(BatchSqlStatementHandler, null)
        }

    }

    data class Select(

        val columns: MutableList<Column> = mutableListOf(),

        var distinct: Boolean = false,

        var count: Boolean = false,

        var from: TableReference? = null,

        val joins: MutableList<JoinTable> = mutableListOf(),

        val where: MutableList<LogicalStatement> = mutableListOf(),

        val groupBy: MutableList<Column> = mutableListOf(),

        val having: MutableList<LogicalStatement> = mutableListOf(),

        val orderBy: MutableList<OrderItem<*>> = mutableListOf(),

        var limit: LimitClause? = null

    ) : DmlNode()

    data class Insert(

        var table: TableReference? = null,

        val columns: MutableList<Column> = mutableListOf(),

        val valuesList: MutableList<MutableList<Any?>> = mutableListOf()

    ) : DmlNode()

    data class Update(

        var table: TableReference? = null,

        val sets: LinkedHashMap<Column, Any?> = linkedMapOf(),

        val valuesList: MutableList<MutableList<Any?>> = mutableListOf(),

        val joins: MutableList<JoinTable> = mutableListOf(),

        val where: MutableList<LogicalStatement> = mutableListOf()

    ) : DmlNode()

    data class Delete(

        var table: TableReference? = null,

        val joins: MutableList<JoinTable> = mutableListOf(),

        val where: MutableList<LogicalStatement> = mutableListOf()

    ) : DmlNode()

}
