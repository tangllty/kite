package com.tang.kite.sql

import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.statement.LogicalStatement

/**
 * @author Tang
 */
sealed class SqlNode {

    class Select(
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

    class Insert(
        val table: TableReference? =  null,
        val columns: MutableList<Column> = mutableListOf(),
        val values: MutableList<Any?> = mutableListOf()
    ) : SqlNode()

    class Update(
        val table: TableReference? = null,
        val sets: LinkedHashMap<String, Any?> = linkedMapOf(),
        val joins: MutableList<JoinTable> = mutableListOf(),
        val where: MutableList<LogicalStatement> = mutableListOf()
    ) : SqlNode()

    class Delete(
        val table: TableReference? = null,
        val joins: MutableList<JoinTable> = mutableListOf(),
        val where: MutableList<LogicalStatement> = mutableListOf()
    ) : SqlNode()

}
