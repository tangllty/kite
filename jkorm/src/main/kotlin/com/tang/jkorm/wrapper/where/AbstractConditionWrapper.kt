package com.tang.jkorm.wrapper.where

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.wrapper.Column
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator
import com.tang.jkorm.wrapper.enumeration.LogicalOperator
import com.tang.jkorm.wrapper.statement.ComparisonStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
abstract class AbstractConditionWrapper<RT, T, R, W>(

    private val conditions: MutableList<LogicalStatement> = mutableListOf()

) : WhereBuilder<T, R, W> {

    @Suppress("UNCHECKED_CAST")
    protected var rtInstance: RT = Any() as RT

    private fun compare(column: Column, value: Any, comparisonOperator: ComparisonOperator, effective: Boolean): RT {
        if (effective) {
            val condition = ComparisonStatement(column, value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return rtInstance
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
    private fun createNestedWrapper(): RT {
        val wrapper = WhereBuilder::class.java.getMethod("build").invoke(rtInstance)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as RT
    }

    /**
     * The where operation
     *
     * @return RT
     */
    fun where() : RT {
        return rtInstance
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun eq(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.EQUAL, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return R
     */
    fun eq(column: String, value: Any, effective: Boolean): RT {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @return R
     */
    fun eq(column: String, value: Any): RT {
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
    fun <E> eq(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> eq(column: KMutableProperty1<E, *>, value: Any): RT {
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
    fun <E> eq(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return eq(Column(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return R
     */
    fun <E> eq(column: SFunction<E, *>, value: Any): RT {
        return eq(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun ne(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.NOT_EQUAL, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun ne(column: String, value: Any, effective: Boolean): RT {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun ne(column: String, value: Any): RT {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> ne(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> ne(column: KMutableProperty1<E, *>, value: Any): RT {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> ne(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return ne(Column(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> ne(column: SFunction<E, *>, value: Any): RT {
        return ne(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun gt(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.GT, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun gt(column: String, value: Any, effective: Boolean): RT {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun gt(column: String, value: Any): RT {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> gt(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> gt(column: KMutableProperty1<E, *>, value: Any): RT {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> gt(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return gt(Column(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> gt(column: SFunction<E, *>, value: Any): RT {
        return gt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun lt(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.LT, effective)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun lt(column: String, value: Any, effective: Boolean): RT {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun lt(column: String, value: Any): RT {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> lt(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> lt(column: KMutableProperty1<E, *>, value: Any): RT {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> lt(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return lt(Column(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> lt(column: SFunction<E, *>, value: Any): RT {
        return lt(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun ge(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.GE, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun ge(column: String, value: Any, effective: Boolean): RT {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun ge(column: String, value: Any): RT {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> ge(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> ge(column: KMutableProperty1<E, *>, value: Any): RT {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> ge(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return ge(Column(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> ge(column: SFunction<E, *>, value: Any): RT {
        return ge(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun le(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, value, ComparisonOperator.LE, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun le(column: String, value: Any, effective: Boolean): RT {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun le(column: String, value: Any): RT {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> le(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> le(column: KMutableProperty1<E, *>, value: Any): RT {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> le(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return le(Column(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> le(column: SFunction<E, *>, value: Any): RT {
        return le(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun like(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "%$value%", ComparisonOperator.LIKE, effective)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun like(column: String, value: Any, effective: Boolean): RT {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun like(column: String, value: Any): RT {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> like(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> like(column: KMutableProperty1<E, *>, value: Any): RT {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> like(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return like(Column(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> like(column: SFunction<E, *>, value: Any): RT {
        return like(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun leftLike(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "%$value", ComparisonOperator.LIKE, effective)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun leftLike(column: String, value: Any, effective: Boolean): RT {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun leftLike(column: String, value: Any): RT {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> leftLike(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> leftLike(column: KMutableProperty1<E, *>, value: Any): RT {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> leftLike(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return leftLike(Column(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> leftLike(column: SFunction<E, *>, value: Any): RT {
        return leftLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun rightLike(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "$value%", ComparisonOperator.LIKE, effective)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun rightLike(column: String, value: Any, effective: Boolean): RT {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun rightLike(column: String, value: Any): RT {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> rightLike(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> rightLike(column: KMutableProperty1<E, *>, value: Any): RT {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> rightLike(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return rightLike(Column(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> rightLike(column: SFunction<E, *>, value: Any): RT {
        return rightLike(column, value, true)
    }

    /**
     * Between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun between(column: Column, start: Any, end: Any, effective: Boolean): RT {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return rtInstance
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun between(column: String, start: Any, end: Any, effective: Boolean): RT {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun between(column: String, start: Any, end: Any): RT {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun <E> between(column: KMutableProperty1<E, *>, start: Any, end: Any, effective: Boolean): RT {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun <E> between(column: KMutableProperty1<E, *>, start: Any, end: Any): RT {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun <E> between(column: SFunction<E, *>, start: Any, end: Any, effective: Boolean): RT {
        return between(Column(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun <E> between(column: SFunction<E, *>, start: Any, end: Any): RT {
        return between(column, start, end, true)
    }

    /**
     * In operation
     *
     * @param column column object
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun `in`(column: Column, values: Iterable<Any>, effective: Boolean): RT {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return rtInstance
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun `in`(column: String, values: Iterable<Any>, effective: Boolean): RT {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return RT
     */
    fun `in`(column: String, values: Iterable<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Iterable<Any>, effective: Boolean): RT {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return RT
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Iterable<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> `in`(column: SFunction<E, *>, values: Iterable<Any>, effective: Boolean): RT {
        return `in`(Column(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return RT
     */
    fun <E> `in`(column: SFunction<E, *>, values: Iterable<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun `in`(column: String, values: Array<Any>, effective: Boolean): RT {
        return `in`(column, values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return RT
     */
    fun `in`(column: String, values: Array<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Array<Any>, effective: Boolean): RT {
        return `in`(Column(column), values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return RT
     */
    fun <E> `in`(column: KMutableProperty1<E, *>, values: Array<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> `in`(column: SFunction<E, *>, values: Array<Any>, effective: Boolean): RT {
        return `in`(Column(column), values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return RT
     */
    fun <E> `in`(column: SFunction<E, *>, values: Array<Any>): RT {
        return `in`(column, values, true)
    }

    /**
     * Not like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notLike(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "%$value%", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notLike(column: String, value: Any, effective: Boolean): RT {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun notLike(column: String, value: Any): RT {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notLike(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> notLike(column: KMutableProperty1<E, *>, value: Any): RT {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notLike(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return notLike(Column(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> notLike(column: SFunction<E, *>, value: Any): RT {
        return notLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notLeftLike(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "%$value", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notLeftLike(column: String, value: Any, effective: Boolean): RT {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun notLeftLike(column: String, value: Any): RT {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notLeftLike(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> notLeftLike(column: KMutableProperty1<E, *>, value: Any): RT {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notLeftLike(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return notLeftLike(Column(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> notLeftLike(column: SFunction<E, *>, value: Any): RT {
        return notLeftLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column object
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notRightLike(column: Column, value: Any, effective: Boolean): RT {
        return compare(column, "$value%", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun notRightLike(column: String, value: Any, effective: Boolean): RT {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @return RT
     */
    fun notRightLike(column: String, value: Any): RT {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notRightLike(column: KMutableProperty1<E, *>, value: Any, effective: Boolean): RT {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @return RT
     */
    fun <E> notRightLike(column: KMutableProperty1<E, *>, value: Any): RT {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notRightLike(column: SFunction<E, *>, value: Any, effective: Boolean): RT {
        return notRightLike(Column(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @return RT
     */
    fun <E> notRightLike(column: SFunction<E, *>, value: Any): RT {
        return notRightLike(column, value, true)
    }

    /**
     * Not between operation
     *
     * @param column column object
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun notBetween(column: Column, start: Any, end: Any, effective: Boolean): RT {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.NOT_BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return rtInstance
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun notBetween(column: String, start: Any, end: Any, effective: Boolean): RT {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun notBetween(column: String, start: Any, end: Any): RT {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notBetween(column: KMutableProperty1<E, *>, start: Any, end: Any, effective: Boolean): RT {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun <E> notBetween(column: KMutableProperty1<E, *>, start: Any, end: Any): RT {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return RT
     */
    fun <E> notBetween(column: SFunction<E, *>, start: Any, end: Any, effective: Boolean): RT {
        return notBetween(Column(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return RT
     */
    fun <E> notBetween(column: SFunction<E, *>, start: Any, end: Any): RT {
        return notBetween(column, start, end, true)
    }

    /**
     * Not in operation
     *
     * @param column column object
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun notIn(column: Column, values: Iterable<Any>, effective: Boolean): RT {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.NOT_IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return rtInstance
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun notIn(column: String, values: Iterable<Any>, effective: Boolean): RT {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return RT
     */
    fun notIn(column: String, values: Iterable<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Iterable<Any>, effective: Boolean): RT {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return RT
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Iterable<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> notIn(column: SFunction<E, *>, values: Iterable<Any>, effective: Boolean): RT {
        return notIn(Column(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return RT
     */
    fun <E> notIn(column: SFunction<E, *>, values: Iterable<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun notIn(column: String, values: Array<Any>, effective: Boolean): RT {
        return notIn(column, values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return RT
     */
    fun notIn(column: String, values: Array<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Array<Any>, effective: Boolean): RT {
        return notIn(Column(column), values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return RT
     */
    fun <E> notIn(column: KMutableProperty1<E, *>, values: Array<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return RT
     */
    fun <E> notIn(column: SFunction<E, *>, values: Array<Any>, effective: Boolean): RT {
        return notIn(Column(column), values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return RT
     */
    fun <E> notIn(column: SFunction<E, *>, values: Array<Any>): RT {
        return notIn(column, values, true)
    }

    /**
     * Is null operation
     *
     * @param column column object
     * @param effective whether effective
     * @return RT
     */
    fun isNull(column: Column, effective: Boolean): RT {
        return compare(column, "", ComparisonOperator.IS_NULL, effective)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return RT
     */
    fun isNull(column: String, effective: Boolean): RT {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @return RT
     */
    fun isNull(column: String): RT {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return RT
     */
    fun <E> isNull(column: KMutableProperty1<E, *>, effective: Boolean): RT {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @return RT
     */
    fun <E> isNull(column: KMutableProperty1<E, *>): RT {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return RT
     */
    fun <E> isNull(column: SFunction<E, *>, effective: Boolean): RT {
        return isNull(Column(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @return RT
     */
    fun <E> isNull(column: SFunction<E, *>): RT {
        return isNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column object
     * @param effective whether effective
     * @return RT
     */
    fun isNotNull(column: Column, effective: Boolean): RT {
        return compare(column, "", ComparisonOperator.IS_NOT_NULL, effective)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return RT
     */
    fun isNotNull(column: String, effective: Boolean): RT {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @return RT
     */
    fun isNotNull(column: String): RT {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return RT
     */
    fun <E> isNotNull(column: KMutableProperty1<E, *>, effective: Boolean): RT {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @return RT
     */
    fun <E> isNotNull(column: KMutableProperty1<E, *>): RT {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return RT
     */
    fun <E> isNotNull(column: SFunction<E, *>, effective: Boolean): RT {
        return isNotNull(Column(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @return RT
     */
    fun <E> isNotNull(column: SFunction<E, *>): RT {
        return isNotNull(column, true)
    }

}
