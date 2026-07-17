package com.tang.kite.sql.function.expression

import com.tang.kite.config.SqlConfig
import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.ColumnArg
import com.tang.kite.sql.function.FunctionRender
import com.tang.kite.sql.function.SqlKeyword
import kotlin.reflect.KProperty1

/**
 * OVER clause builder for window functions.
 *
 * @author Tang
 */
class OverClause : FunctionRender {

    private var partitionBy: MutableList<ColumnArg> = mutableListOf()

    private var orderByItems: MutableList<Pair<ColumnArg, Boolean>> = mutableListOf()

    /**
     * Adds a column to the PARTITION BY clause.
     *
     * @param columnName Column name to partition by
     * @return this OverClause for chaining
     */
    fun partitionBy(columnName: String): OverClause {
        partitionBy.add(ColumnArg(Column(columnName)))
        return this
    }

    /**
     * Adds a column to the PARTITION BY clause using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this OverClause for chaining
     */
    fun partitionBy(column: KProperty1<*, *>): OverClause {
        return partitionBy(Column(column))
    }

    /**
     * Adds a column to the PARTITION BY clause using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this OverClause for chaining
     */
    fun partitionBy(column: SFunction<*, *>): OverClause {
        return partitionBy(Column(column))
    }

    /**
     * Adds a column to the PARTITION BY clause.
     *
     * @param column Column object
     * @return this OverClause for chaining
     */
    fun partitionBy(column: Column): OverClause {
        partitionBy.add(ColumnArg(column))
        return this
    }

    /**
     * Adds a column to the ORDER BY clause (ascending order by default).
     *
     * @param columnName Column name to order by
     * @return this OverClause for chaining
     */
    fun orderBy(columnName: String): OverClause {
        orderByItems.add(Pair(ColumnArg(Column(columnName)), false))
        return this
    }

    /**
     * Adds a column to the ORDER BY clause using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this OverClause for chaining
     */
    fun orderBy(column: KProperty1<*, *>): OverClause {
        return orderBy(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this OverClause for chaining
     */
    fun orderBy(column: SFunction<*, *>): OverClause {
        return orderBy(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause.
     *
     * @param column Column object
     * @return this OverClause for chaining
     */
    fun orderBy(column: Column): OverClause {
        orderByItems.add(Pair(ColumnArg(column), false))
        return this
    }

    /**
     * Adds a column to the ORDER BY clause in descending order.
     *
     * @param columnName Column name to order by (descending)
     * @return this OverClause for chaining
     */
    fun orderByDesc(columnName: String): OverClause {
        orderByItems.add(Pair(ColumnArg(Column(columnName)), true))
        return this
    }

    /**
     * Adds a column to the ORDER BY clause in descending order using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this OverClause for chaining
     */
    fun orderByDesc(column: KProperty1<*, *>): OverClause {
        return orderByDesc(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause in descending order using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this OverClause for chaining
     */
    fun orderByDesc(column: SFunction<*, *>): OverClause {
        return orderByDesc(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause in descending order.
     *
     * @param column Column object
     * @return this OverClause for chaining
     */
    fun orderByDesc(column: Column): OverClause {
        orderByItems.add(Pair(ColumnArg(column), true))
        return this
    }

    /**
     * Renders the OVER clause as SQL string.
     *
     * @return SQL string representation of the OVER clause
     */
    override fun render(): String {
        val sb = StringBuilder(SqlConfig.getSql(SqlKeyword.OVER))
        sb.append("(")

        val parts = mutableListOf<String>()

        if (partitionBy.isNotEmpty()) {
            val partitionText = partitionBy.joinToString(", ") { it.render() }
            parts.add("${SqlConfig.getSql(SqlKeyword.PARTITION_BY)} $partitionText")
        }

        if (orderByItems.isNotEmpty()) {
            val orderParts = mutableListOf<String>()
            orderByItems.forEach { (column, isDesc) ->
                if (isDesc) {
                    orderParts.add("${column.render()} ${SqlConfig.getSql(SqlKeyword.DESC)}")
                } else {
                    orderParts.add(column.render())
                }
            }
            parts.add("${SqlConfig.getSql(SqlKeyword.ORDER_BY)} ${orderParts.joinToString(", ")}")
        }

        sb.append(parts.joinToString(" "))
        sb.append(")")

        return sb.toString()
    }

}
