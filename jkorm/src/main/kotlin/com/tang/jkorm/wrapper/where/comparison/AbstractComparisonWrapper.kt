package com.tang.jkorm.wrapper.where.comparison

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator
import com.tang.jkorm.wrapper.enumeration.LogicalOperator
import com.tang.jkorm.wrapper.statement.ComparisonStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import com.tang.jkorm.wrapper.where.WhereBuilder
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Base class for comparison wrapper
 *
 * @author Tang
 */
abstract class AbstractComparisonWrapper<T, R, W>(private val conditions: MutableList<LogicalStatement>) : WhereBuilder<T, R, W> {

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param logicalOperator logical operator
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: String, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, value, ComparisonOperator.EQUAL)
            conditions.add(LogicalStatement(condition, logicalOperator))
        }
        return this
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param logicalOperator logical operator
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: String, value: Any, logicalOperator: LogicalOperator): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, logicalOperator, true)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: String, value: Any, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: String, value: Any): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param logicalOperator logical operator
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        if (effective) {
            var filed = column.javaField!!
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param logicalOperator logical operator
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, logicalOperator, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param logicalOperator logical operator
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        if (effective) {
            val filed = Fields.getField(column)
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param logicalOperator logical operator
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, logicalOperator, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any): AbstractComparisonWrapper<T, R, W> {
        return eq(column, value, LogicalOperator.AND)
    }

    private fun setLastLogicalOperator(logicalOperator: LogicalOperator) {
        if (conditions.isEmpty()) {
            return
        }
        if (conditions.last().nestedConditions.isEmpty()) {
            conditions.last().logicalOperator = logicalOperator
        } else {
            conditions.last().nestedLogicalOperator = logicalOperator
        }
    }

    /**
     * And operation
     *
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun and(): AbstractComparisonWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.AND)
        return this
    }

    /**
     * Or operation
     *
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun or(): AbstractComparisonWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.OR)
        return this
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun and(nested: AbstractComparisonWrapper<T, R, W>.() -> Unit): AbstractComparisonWrapper<T, R, W> {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        if (nestedWrapper.conditions.isEmpty()) {
            return this
        }
        conditions.last().logicalOperator = LogicalOperator.AND
        conditions.last().nestedConditions = nestedWrapper.conditions
        return this
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun and(nested: Consumer<AbstractComparisonWrapper<T, R, W>>): AbstractComparisonWrapper<T, R, W> {
        return and { nested.accept(this) }
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun or(nested: AbstractComparisonWrapper<T, R, W>.() -> Unit): AbstractComparisonWrapper<T, R, W> {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR
        conditions.last().nestedConditions = nestedWrapper.conditions
        return this
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return AbstractComparisonWrapper<T, R, W>
     */
    fun or(nested: Consumer<AbstractComparisonWrapper<T, R, W>>): AbstractComparisonWrapper<T, R, W> {
        return or { nested.accept(this) }
    }

    private fun createNestedWrapper(): AbstractComparisonWrapper<T, R, W> {
        val wrapper = WhereBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractComparisonWrapper<T, R, W>
    }

}
