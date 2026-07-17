package com.tang.kite.sql.function.expression

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.FunctionRender
import kotlin.reflect.KProperty1

/**
 * Window function expression with OVER clause support.
 *
 * @author Tang
 */
class WindowFunctionExpression(

    function: String,

    renderList: MutableList<FunctionRender> = mutableListOf(),

    withoutParentheses: Boolean = false

) : FunctionExpression(function, renderList, withoutParentheses) {

    /**
     * Constructor for window functions with variable arguments.
     *
     * @param function Function name
     * @param renders Variable list of function arguments
     */
    constructor(function: String, vararg renders: FunctionRender) : this(function, mutableListOf(*renders))

    private var overClause: OverClause? = null

    /**
     * Ensures OverClause is initialized, creating it if necessary.
     *
     * @return The existing or newly created OverClause
     */
    private fun ensureOverClause(): OverClause {
        if (overClause == null) {
            overClause = OverClause()
        }
        return overClause!!
    }

    /**
     * Adds an empty OVER clause to the window function.
     *
     * Example:
     * ```kotlin
     * SqlFunction.rowNumber().over()  // row_number() over()
     * ```
     *
     * @return this WindowFunctionExpression for chaining
     */
    fun over(): WindowFunctionExpression {
        ensureOverClause()
        return this
    }

    /**
     * Adds an OVER clause with the specified configuration.
     *
     * Example:
     * ```kotlin
     * SqlFunction.rank().over {
     *     partitionBy("department")
     *     orderByDesc("salary")
     * }
     * // rank() over(partition by department order by salary desc)
     * ```
     *
     * @param block Lambda to configure the OverClause
     * @return this WindowFunctionExpression for chaining
     */
    fun over(block: OverClause.() -> Unit): WindowFunctionExpression {
        this.overClause = OverClause().apply(block)
        return this
    }

    /**
     * Adds a pre-configured OVER clause to the window function.
     *
     * @param overClause Pre-configured OverClause instance
     * @return this WindowFunctionExpression for chaining
     */
    fun over(overClause: OverClause): WindowFunctionExpression {
        this.overClause = overClause
        return this
    }

    /**
     * Renders the window function as SQL string.
     *
     * If no OVER clause is specified, only the function itself is rendered.
     * Note: Window functions in SQL typically require an OVER clause.
     *
     * @return SQL string representation of the window function
     */
    override fun render(): String {
        val functionBase = super.render()
        if (overClause != null) {
            return "$functionBase ${overClause?.render()}"
        }
        return functionBase
    }

    /**
     * Adds a column to the PARTITION BY clause.
     *
     * This is a convenience method that delegates to the internal OverClause.
     *
     * @param columnName Column name to partition by
     * @return this WindowFunctionExpression for chaining
     */
    fun partitionBy(columnName: String): WindowFunctionExpression {
        ensureOverClause().partitionBy(columnName)
        return this
    }

    /**
     * Adds a column to the PARTITION BY clause using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this WindowFunctionExpression for chaining
     */
    fun partitionBy(column: KProperty1<*, *>): WindowFunctionExpression {
        return partitionBy(Column(column))
    }

    /**
     * Adds a column to the PARTITION BY clause using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this WindowFunctionExpression for chaining
     */
    fun partitionBy(column: SFunction<*, *>): WindowFunctionExpression {
        return partitionBy(Column(column))
    }

    /**
     * Adds a column to the PARTITION BY clause.
     *
     * @param column Column object
     * @return this WindowFunctionExpression for chaining
     */
    fun partitionBy(column: Column): WindowFunctionExpression {
        ensureOverClause().partitionBy(column)
        return this
    }

    /**
     * Adds a column to the ORDER BY clause (ascending order by default).
     *
     * This is a convenience method that delegates to the internal OverClause.
     *
     * @param columnName Column name to order by
     * @return this WindowFunctionExpression for chaining
     */
    fun orderBy(columnName: String): WindowFunctionExpression {
        ensureOverClause().orderBy(columnName)
        return this
    }

    /**
     * Adds a column to the ORDER BY clause using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this WindowFunctionExpression for chaining
     */
    fun orderBy(column: KProperty1<*, *>): WindowFunctionExpression {
        return orderBy(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this WindowFunctionExpression for chaining
     */
    fun orderBy(column: SFunction<*, *>): WindowFunctionExpression {
        return orderBy(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause.
     *
     * @param column Column object
     * @return this WindowFunctionExpression for chaining
     */
    fun orderBy(column: Column): WindowFunctionExpression {
        ensureOverClause().orderBy(column)
        return this
    }

    /**
     * Adds a column to the ORDER BY clause in descending order.
     *
     * This is a convenience method that delegates to the internal OverClause.
     *
     * @param columnName Column name to order by (descending)
     * @return this WindowFunctionExpression for chaining
     */
    fun orderByDesc(columnName: String): WindowFunctionExpression {
        ensureOverClause().orderByDesc(columnName)
        return this
    }

    /**
     * Adds a column to the ORDER BY clause in descending order using Kotlin property reference.
     *
     * @param column Kotlin property reference
     * @return this WindowFunctionExpression for chaining
     */
    fun orderByDesc(column: KProperty1<*, *>): WindowFunctionExpression {
        return orderByDesc(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause in descending order using lambda expression.
     *
     * @param column Lambda expression representing the column
     * @return this WindowFunctionExpression for chaining
     */
    fun orderByDesc(column: SFunction<*, *>): WindowFunctionExpression {
        return orderByDesc(Column(column))
    }

    /**
     * Adds a column to the ORDER BY clause in descending order.
     *
     * @param column Column object
     * @return this WindowFunctionExpression for chaining
     */
    fun orderByDesc(column: Column): WindowFunctionExpression {
        ensureOverClause().orderByDesc(column)
        return this
    }

}
