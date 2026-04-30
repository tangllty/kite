package com.tang.kite.sql.ast

import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.ast.ddl.DdlHandler
import com.tang.kite.sql.ast.ddl.SqlStatementDdlHandler
import com.tang.kite.sql.ast.dml.BatchSqlStatementDmlHandler
import com.tang.kite.sql.ast.dml.DmlHandler
import com.tang.kite.sql.ast.dml.SqlStatementDmlHandler
import com.tang.kite.sql.ast.dql.DqlHandler
import com.tang.kite.sql.ast.dql.SqlStatementDqlHandler
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.sql.statement.SqlStatement

/**
 * @author Tang
 */
sealed class SqlNode {

    sealed class DqlNode : SqlNode() {

        fun <T> accept(handler: DqlHandler<T>, dialect: SqlDialect): T {
            return when (this) {
                is Select -> handler.handleSelect(this, dialect)
            }
        }

        fun getSqlStatement(dialect: SqlDialect): SqlStatement {
            return accept(SqlStatementDqlHandler, dialect)
        }

    }

    sealed class DmlNode : SqlNode() {

        fun <T> accept(handler: DmlHandler<T>): T {
            return when (this) {
                is Insert -> handler.handleInsert(this)
                is Update -> handler.handleUpdate(this)
                is Delete -> handler.handleDelete(this)
            }
        }

        fun getSqlStatement(): SqlStatement {
            return accept(SqlStatementDmlHandler)
        }

        fun getBatchSqlStatement(): BatchSqlStatement {
            return accept(BatchSqlStatementDmlHandler)
        }

    }

    sealed class DdlNode : SqlNode() {

        fun <T> accept(handler: DdlHandler<T>, dialect: SqlDialect): T {
            return when (this) {
                is CreateTable -> handler.handleCreateTable(this, dialect)
                is AlterTable -> handler.handleAlterTable(this, dialect)
                is DropTable -> handler.handleDropTable(this, dialect)
                is CreateIndex -> handler.handleCreateIndex(this, dialect)
                is DropIndex -> handler.handleDropIndex(this, dialect)
                is TruncateTable -> handler.handleTruncateTable(this, dialect)
            }
        }

        fun getSqlList(dialect: SqlDialect): List<String> {
            return accept(SqlStatementDdlHandler, dialect)
        }

        fun getFirstSql(dialect: SqlDialect): String {
            return getSqlList(dialect).first()
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

    ) : DqlNode()

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

    data class CreateTable(

        var table: TableReference? = null,

        val columns: MutableList<ColumnMeta> = mutableListOf(),

        val constraints: MutableList<TableConstraint> = mutableListOf(),

        val createIndexes: MutableList<CreateIndex> = mutableListOf(),

        var comment: String? = null

    ) : DdlNode()

    data class AlterTable(

        var table: TableReference? = null,

        val operations: MutableList<AlterOperation> = mutableListOf()

    ) : DdlNode()

    data class DropTable(

        var table: TableReference? = null,

        var cascade: Boolean = false

    ) : DdlNode()

    data class CreateIndex(

        var indexName: String,

        var table: TableReference,

        val columns: List<String>,

        var unique: Boolean = false,

    ) : DdlNode()

    data class DropIndex(

        var indexName: String? = null,

        var table: TableReference? = null

    ) : DdlNode()

    data class TruncateTable(

        var table: TableReference? = null,

        var cascade: Boolean = false

    ) : DdlNode()

}
