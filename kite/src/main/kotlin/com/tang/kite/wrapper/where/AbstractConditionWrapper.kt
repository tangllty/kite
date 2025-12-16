package com.tang.kite.wrapper.where

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.function.FunctionColumn
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
abstract class AbstractConditionWrapper<R, T> : WrapperBuilder<T> {

    protected var conditions: MutableList<LogicalStatement> = mutableListOf()

    protected var conditionInstance: R? = null

    private fun getInstance(): R {
        conditionInstance ?: initialize(conditions)
        return conditionInstance ?: throw IllegalStateException("Condition instance is not initialized")
    }

    open fun initialize(conditions: MutableList<LogicalStatement>) {
        throw UnsupportedOperationException("This method should be overridden in subclasses")
    }

    private fun compare(column: Column, value: Any?, comparisonOperator: ComparisonOperator, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(column, value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    protected fun setLastLogicalOperator(logicalOperator: LogicalOperator) {
        if (conditions.isEmpty()) {
            return
        }
        if (conditions.last().nestedConditions.isEmpty()) {
            conditions.last().logicalOperator = logicalOperator
        } else {
            conditions.last().nestedLogicalOperator = logicalOperator
        }
    }

    @Deprecated("May cannot be used in this class")
    @Suppress("UNCHECKED_CAST")
    private fun createNestedWrapper(): R {
        val wrapper = WrapperBuilder::class.java.getMethod("build").invoke(conditionInstance)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as R
    }

    /**
     * The where operation
     *
     * @return R
     */
    fun where(): R {
        return getInstance()
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun eq(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.EQUAL, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun eq(column: Column, value: Any?): R {
        return eq(column, value, true)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun eq(column: String, value: Any?, effective: Boolean): R {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun eq(column: String, value: Any?): R {
        return eq(column, value, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> eq(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> eq(column: KMutableProperty1<E, *>, value: Any?): R {
        return eq(column, value, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> eq(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> eq(column: SFunction<E, *>, value: Any?): R {
        return eq(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun ne(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.NOT_EQUAL, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun ne(column: Column, value: Any?): R {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun ne(column: String, value: Any?, effective: Boolean): R {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun ne(column: String, value: Any?): R {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> ne(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> ne(column: KMutableProperty1<E, *>, value: Any?): R {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> ne(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> ne(column: SFunction<E, *>, value: Any?): R {
        return ne(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun gt(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.GT, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun gt(column: Column, value: Any?): R {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun gt(column: String, value: Any?, effective: Boolean): R {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun gt(column: String, value: Any?): R {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> gt(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> gt(column: KMutableProperty1<E, *>, value: Any?): R {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> gt(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> gt(column: SFunction<E, *>, value: Any?): R {
        return gt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun lt(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.LT, effective)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun lt(column: Column, value: Any?): R {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun lt(column: String, value: Any?, effective: Boolean): R {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun lt(column: String, value: Any?): R {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> lt(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> lt(column: KMutableProperty1<E, *>, value: Any?): R {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> lt(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> lt(column: SFunction<E, *>, value: Any?): R {
        return lt(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun ge(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.GE, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun ge(column: Column, value: Any?): R {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun ge(column: String, value: Any?, effective: Boolean): R {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun ge(column: String, value: Any?): R {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> ge(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> ge(column: KMutableProperty1<E, *>, value: Any?): R {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> ge(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> ge(column: SFunction<E, *>, value: Any?): R {
        return ge(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun le(column: Column, value: Any?, effective: Boolean): R {
        return compare(column, value, ComparisonOperator.LE, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun le(column: Column, value: Any?): R {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun le(column: String, value: Any?, effective: Boolean): R {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun le(column: String, value: Any?): R {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> le(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> le(column: KMutableProperty1<E, *>, value: Any?): R {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> le(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> le(column: SFunction<E, *>, value: Any?): R {
        return le(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun like(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "%${value.column}%"
        }
        return compare(column, value, ComparisonOperator.LIKE, effective)
    }

    /**
     * Like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun like(column: Column, value: Any?): R {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun like(column: String, value: Any?, effective: Boolean): R {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun like(column: String, value: Any?): R {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> like(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> like(column: KMutableProperty1<E, *>, value: Any?): R {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> like(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> like(column: SFunction<E, *>, value: Any?): R {
        return like(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun leftLike(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "%${value.column}"
        }
        return compare(column, value, ComparisonOperator.LIKE, effective)
    }

    /**
     * Left like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun leftLike(column: Column, value: Any?): R {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun leftLike(column: String, value: Any?, effective: Boolean): R {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun leftLike(column: String, value: Any?): R {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> leftLike(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> leftLike(column: KMutableProperty1<E, *>, value: Any?): R {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> leftLike(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> leftLike(column: SFunction<E, *>, value: Any?): R {
        return leftLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun rightLike(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "${value.column}%"
        }
        return compare(column, value, ComparisonOperator.LIKE, effective)
    }

    /**
     * Right like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun rightLike(column: Column, value: Any?): R {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun rightLike(column: String, value: Any?, effective: Boolean): R {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun rightLike(column: String, value: Any?): R {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> rightLike(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> rightLike(column: KMutableProperty1<E, *>, value: Any?): R {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> rightLike(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> rightLike(column: SFunction<E, *>, value: Any?): R {
        return rightLike(column, value, true)
    }

    /**
     * Between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun between(column: Column, start: Any, end: Any, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    /**
     * Between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @return R
     */
    fun between(column: Column, start: Any, end: Any): R {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun between(column: String, start: Any, end: Any, effective: Boolean): R {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return R
     */
    fun between(column: String, start: Any, end: Any): R {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun <E> between(column: KMutableProperty1<E, *>, start: Any, end: Any, effective: Boolean): R {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return R
     */
    fun <E> between(column: KMutableProperty1<E, *>, start: Any, end: Any): R {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun <E> between(column: SFunction<E, *>, start: Any, end: Any, effective: Boolean): R {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return R
     */
    fun <E> between(column: SFunction<E, *>, start: Any, end: Any): R {
        return between(column, start, end, true)
    }

    /**
     * In operation
     *
     * @param column column object
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun `in`(column: Column, values: Iterable<Any?>, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    /**
     * In operation
     *
     * @param column column object
     * @param values values
     * @return R
     */
    fun `in`(column: Column, values: Iterable<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun `in`(column: String, values: Iterable<Any?>, effective: Boolean): R {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return R
     */
    fun `in`(column: String, values: Iterable<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Iterable<Any?>, effective: Boolean): R {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return R
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Iterable<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> `in`(column: SFunction<E, *>, values: Iterable<Any?>, effective: Boolean): R {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return R
     */
    fun <E> `in`(column: SFunction<E, *>, values: Iterable<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun `in`(column: String, values: Array<Any?>, effective: Boolean): R {
        return `in`(column, values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return R
     */
    fun `in`(column: String, values: Array<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Array<Any?>, effective: Boolean): R {
        return `in`(Column(column), values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return R
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Array<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> `in`(column: SFunction<E, *>, values: Array<Any?>, effective: Boolean): R {
        return `in`(Column(column), values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return R
     */
    fun <E> `in`(column: SFunction<E, *>, values: Array<Any?>): R {
        return `in`(column, values, true)
    }

    /**
     * Not like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notLike(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "%${value.column}%"
        }
        return compare(column, value, ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun notLike(column: Column, value: Any?): R {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notLike(column: String, value: Any?, effective: Boolean): R {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun notLike(column: String, value: Any?): R {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notLike(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> notLike(column: KMutableProperty1<E, *>, value: Any?): R {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notLike(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> notLike(column: SFunction<E, *>, value: Any?): R {
        return notLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notLeftLike(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "%${value.column}"
        }
        return compare(column, value, ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun notLeftLike(column: Column, value: Any?): R {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notLeftLike(column: String, value: Any?, effective: Boolean): R {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun notLeftLike(column: String, value: Any?): R {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notLeftLike(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> notLeftLike(column: KMutableProperty1<E, *>, value: Any?): R {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notLeftLike(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> notLeftLike(column: SFunction<E, *>, value: Any?): R {
        return notLeftLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notRightLike(column: Column, value: Any?, effective: Boolean): R {
        if (value is FunctionColumn) {
            value.column = "${value.column}%"
        }
        return compare(column, value, ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column object
     * @param value value
     * @return R
     */
    fun notRightLike(column: Column, value: Any?): R {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun notRightLike(column: String, value: Any?, effective: Boolean): R {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun notRightLike(column: String, value: Any?): R {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notRightLike(column: KMutableProperty1<E, *>, value: Any?, effective: Boolean): R {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> notRightLike(column: KMutableProperty1<E, *>, value: Any?): R {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun <E> notRightLike(column: SFunction<E, *>, value: Any?, effective: Boolean): R {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @return R
     */
    fun <E> notRightLike(column: SFunction<E, *>, value: Any?): R {
        return notRightLike(column, value, true)
    }

    /**
     * Not between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun notBetween(column: Column, start: Any, end: Any, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.NOT_BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    /**
     * Not between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @return R
     */
    fun notBetween(column: Column, start: Any, end: Any): R {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun notBetween(column: String, start: Any, end: Any, effective: Boolean): R {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return R
     */
    fun notBetween(column: String, start: Any, end: Any): R {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun <E> notBetween(column: KMutableProperty1<E, *>, start: Any, end: Any, effective: Boolean): R {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return R
     */
    fun <E> notBetween(column: KMutableProperty1<E, *>, start: Any, end: Any): R {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return R
     */
    fun <E> notBetween(column: SFunction<E, *>, start: Any, end: Any, effective: Boolean): R {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return R
     */
    fun <E> notBetween(column: SFunction<E, *>, start: Any, end: Any): R {
        return notBetween(column, start, end, true)
    }

    /**
     * Not in operation
     *
     * @param column column object
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun notIn(column: Column, values: Iterable<Any?>, effective: Boolean): R {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.NOT_IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return getInstance()
    }

    /**
     * Not in operation
     *
     * @param column column object
     * @param values values
     * @return R
     */
    fun notIn(column: Column, values: Iterable<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun notIn(column: String, values: Iterable<Any?>, effective: Boolean): R {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return R
     */
    fun notIn(column: String, values: Iterable<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Iterable<Any?>, effective: Boolean): R {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return R
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Iterable<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> notIn(column: SFunction<E, *>, values: Iterable<Any?>, effective: Boolean): R {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return R
     */
    fun <E> notIn(column: SFunction<E, *>, values: Iterable<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun notIn(column: String, values: Array<Any?>, effective: Boolean): R {
        return notIn(column, values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return R
     */
    fun notIn(column: String, values: Array<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Array<Any?>, effective: Boolean): R {
        return notIn(Column(column), values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return R
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Array<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return R
     */
    fun <E> notIn(column: SFunction<E, *>, values: Array<Any?>, effective: Boolean): R {
        return notIn(Column(column), values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return R
     */
    fun <E> notIn(column: SFunction<E, *>, values: Array<Any?>): R {
        return notIn(column, values, true)
    }

    /**
     * Is null operation
     *
     * @param column column object
     * @param effective whether effective
     * @return R
     */
    fun isNull(column: Column, effective: Boolean): R {
        return compare(column, "", ComparisonOperator.IS_NULL, effective)
    }

    /**
     * Is null operation
     *
     * @param column column object
     * @return R
     */
    fun isNull(column: Column): R {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return R
     */
    fun isNull(column: String, effective: Boolean): R {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @return R
     */
    fun isNull(column: String): R {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return R
     */
    fun <E> isNull(column: KMutableProperty1<E, *>, effective: Boolean): R {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @return R
     */
    fun <E> isNull(column: KMutableProperty1<E, *>): R {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return R
     */
    fun <E> isNull(column: SFunction<E, *>, effective: Boolean): R {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @return R
     */
    fun <E> isNull(column: SFunction<E, *>): R {
        return isNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column object
     * @param effective whether effective
     * @return R
     */
    fun isNotNull(column: Column, effective: Boolean): R {
        return compare(column, "", ComparisonOperator.IS_NOT_NULL, effective)
    }

    /**
     * Is not null operation
     *
     * @param column column object
     * @return R
     */
    fun isNotNull(column: Column): R {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return R
     */
    fun isNotNull(column: String, effective: Boolean): R {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @return R
     */
    fun isNotNull(column: String): R {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return R
     */
    fun <E> isNotNull(column: KMutableProperty1<E, *>, effective: Boolean): R {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @return R
     */
    fun <E> isNotNull(column: KMutableProperty1<E, *>): R {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return R
     */
    fun <E> isNotNull(column: SFunction<E, *>, effective: Boolean): R {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @return R
     */
    fun <E> isNotNull(column: SFunction<E, *>): R {
        return isNotNull(column, true)
    }

}
