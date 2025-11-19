package com.tang.kite.wrapper.where

import com.tang.kite.sql.Column
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.Wrapper
import java.util.function.Consumer

/**
 * @author Tang
 */
abstract class AbstractHavingWrapper<R, T>(private val wrapper: Wrapper<T>) : AbstractConditionWrapper<R, T>() {

    protected var havingInstance: R? = null

    private fun getInstance(): R {
        return havingInstance ?: throw IllegalStateException("Having instance is not initialized")
    }

    /**
     * And operation
     *
     * @return R
     */
    fun and(): R {
        setLastLogicalOperator(LogicalOperator.AND)
        return getInstance()
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun and(nested: AbstractHavingWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        if (nestedWrapper.conditions.isEmpty()) {
            return getInstance()
        }
        conditions.last().logicalOperator = LogicalOperator.AND
        conditions.last().nestedConditions = nestedWrapper.conditions
        return getInstance()
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun and(nested: Consumer<AbstractHavingWrapper<R, T>>): R {
        return and { nested.accept(this) }
    }

    /**
     * Or operation
     *
     * @return R
     */
    fun or(): R {
        setLastLogicalOperator(LogicalOperator.OR)
        return getInstance()
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun or(nested: AbstractHavingWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR
        conditions.last().nestedConditions = nestedWrapper.conditions
        return getInstance()
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun or(nested: Consumer<AbstractHavingWrapper<R, T>>): R {
        return or { nested.accept(this) }
    }

    /**
     * And not operation
     *
     * @return R
     */
    fun andNot(): R {
        setLastLogicalOperator(LogicalOperator.AND_NOT)
        return getInstance()
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun andNot(nested: AbstractHavingWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.AND_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return getInstance()
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun andNot(nested: Consumer<AbstractHavingWrapper<R, T>>): R {
        return andNot { nested.accept(this) }
    }

    /**
     * Or not operation
     *
     * @return R
     */
    fun orNot(): R {
        setLastLogicalOperator(LogicalOperator.OR_NOT)
        return getInstance()
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun orNot(nested: AbstractHavingWrapper<R, T>.() -> Unit): R {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return getInstance()
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return R
     */
    fun orNot(nested: Consumer<AbstractHavingWrapper<R, T>>): R {
        return orNot { nested.accept(this) }
    }

    private fun createNestedWrapper(): AbstractHavingWrapper<R, T> {
        val wrapper = WrapperBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractHavingWrapper<R, T>
    }

    private fun compare(column: String, value: Any, comparisonOperator: ComparisonOperator, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(Column(column), value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): Wrapper<T> {
        return wrapper
    }

    fun appendSqlNode(orderBy: MutableList<LogicalStatement>) {
        orderBy.addAll(conditions)
    }

}
