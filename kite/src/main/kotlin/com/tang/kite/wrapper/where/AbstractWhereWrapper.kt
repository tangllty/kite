package com.tang.kite.wrapper.where

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.sql.SqlStatement
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.enumeration.LogicalOperator
import com.tang.kite.wrapper.statement.LogicalStatement
import java.util.function.Consumer

/**
 * Base class for where wrapper
 *
 * @author Tang
 */
abstract class AbstractWhereWrapper<R, T>(

    private val wrapper: Wrapper<T>,

    private val conditions: MutableList<LogicalStatement>

) : AbstractConditionWrapper<R, T>(conditions) {

    @Suppress("UNCHECKED_CAST")
    protected var whereInstance: R = Any() as R

    private fun createNestedWrapper(): AbstractWhereWrapper<R, T> {
        val wrapper = WrapperBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractWhereWrapper<R, T>
    }

    /**
     * And operation
     *
     * @return R
     */
    fun and(): R {
        setLastLogicalOperator(LogicalOperator.AND)
        return whereInstance
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun and(nested: AbstractWhereWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        if (nestedWrapper.conditions.isEmpty()) {
            return whereInstance
        }
        conditions.last().logicalOperator = LogicalOperator.AND
        conditions.last().nestedConditions = nestedWrapper.conditions
        return whereInstance
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun and(nested: Consumer<AbstractWhereWrapper<R, T>>): R {
        return and { nested.accept(this) }
    }

    /**
     * Or operation
     *
     * @return R
     */
    fun or(): AbstractWhereWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.OR)
        return this
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun or(nested: AbstractWhereWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR
        conditions.last().nestedConditions = nestedWrapper.conditions
        return whereInstance
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun or(nested: Consumer<AbstractWhereWrapper<R, T>>): R {
        return or { nested.accept(this) }
    }

    /**
     * And not operation
     *
     * @return R
     */
    fun andNot(): AbstractWhereWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.AND_NOT)
        return this
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun andNot(nested: AbstractWhereWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.AND_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return whereInstance
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun andNot(nested: Consumer<AbstractWhereWrapper<R, T>>): R {
        return andNot { nested.accept(this) }
    }

    /**
     * Or not operation
     *
     * @return R
     */
    fun orNot(): AbstractWhereWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.OR_NOT)
        return this
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun orNot(nested: AbstractWhereWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return whereInstance
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun orNot(nested: Consumer<AbstractWhereWrapper<R, T>>): R {
        return orNot { nested.accept(this) }
    }

    /**
     * Get the SQL statement
     *
     * @return SqlStatement
     */
    fun getSqlStatement(): SqlStatement {
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        appendSql(sql, parameters)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): Wrapper<T> {
        return wrapper
    }

    open fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, multiTableQuery: Boolean = false) {
        if (conditions.isNotEmpty()) {
            sql.append(WHERE)
            if (conditions.last().nestedConditions.isEmpty()) {
                conditions.last().logicalOperator = null
            }
            conditions.forEach {
                it.appendSql(sql, parameters, multiTableQuery)
            }
        }
    }

}
