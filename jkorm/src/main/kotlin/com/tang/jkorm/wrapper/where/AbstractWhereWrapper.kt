package com.tang.jkorm.wrapper.where

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Reflects.getColumnName
import com.tang.jkorm.wrapper.Wrapper
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator
import com.tang.jkorm.wrapper.enumeration.LogicalOperator
import com.tang.jkorm.wrapper.statement.ComparisonStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty1

/**
 * Base class for where wrapper
 *
 * @author Tang
 */
abstract class AbstractWhereWrapper<T, R, W>(

    private val wrapper: Wrapper<T>,

    private val conditions: MutableList<LogicalStatement>

) : WhereBuilder<T, R, W> {

    protected lateinit var whereGroupByWrapper: WhereGroupByWrapper<T, R, W>

    lateinit var whereHavingWrapper: WhereHavingWrapper<T, R, W>

    lateinit var whereOrderByWrapper: WhereOrderByWrapper<T, R, W>

    protected fun isGroupByInitialized(): Boolean {
        return this::whereGroupByWrapper.isInitialized
    }

    protected fun isHavingInitialized(): Boolean {
        return this::whereHavingWrapper.isInitialized
    }

    protected fun isOrderByInitialized(): Boolean {
        return this::whereOrderByWrapper.isInitialized
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
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun and(): AbstractWhereWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.AND)
        return this
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun and(nested: AbstractWhereWrapper<T, R, W>.() -> Unit): AbstractWhereWrapper<T, R, W> {
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
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun and(nested: Consumer<AbstractWhereWrapper<T, R, W>>): AbstractWhereWrapper<T, R, W> {
        return and { nested.accept(this) }
    }

    /**
     * Or operation
     *
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun or(): AbstractWhereWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.OR)
        return this
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun or(nested: AbstractWhereWrapper<T, R, W>.() -> Unit): AbstractWhereWrapper<T, R, W> {
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
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun or(nested: Consumer<AbstractWhereWrapper<T, R, W>>): AbstractWhereWrapper<T, R, W> {
        return or { nested.accept(this) }
    }

    /**
     * And not operation
     *
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun andNot(): AbstractWhereWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.AND_NOT)
        return this
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun andNot(nested: AbstractWhereWrapper<T, R, W>.() -> Unit): AbstractWhereWrapper<T, R, W> {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.AND_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return this
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun andNot(nested: Consumer<AbstractWhereWrapper<T, R, W>>): AbstractWhereWrapper<T, R, W> {
        return andNot { nested.accept(this) }
    }

    /**
     * Or not operation
     *
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun orNot(): AbstractWhereWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.OR_NOT)
        return this
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun orNot(nested: AbstractWhereWrapper<T, R, W>.() -> Unit): AbstractWhereWrapper<T, R, W> {
        val nestedWrapper = createNestedWrapper()
        nestedWrapper.nested()
        conditions.last().logicalOperator = LogicalOperator.OR_NOT
        conditions.last().nestedConditions = nestedWrapper.conditions
        return this
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun orNot(nested: Consumer<AbstractWhereWrapper<T, R, W>>): AbstractWhereWrapper<T, R, W> {
        return orNot { nested.accept(this) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createNestedWrapper(): AbstractWhereWrapper<T, R, W> {
        val wrapper = WhereBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractWhereWrapper<T, R, W>
    }

    private fun compare(column: String, value: Any, comparisonOperator: ComparisonOperator, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.EQUAL, effective)
    }

    /**
     * Equal operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return eq(column, value, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return eq(getColumnName(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return eq(column, value, true)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return eq(getColumnName(column), value, effective)
    }

    /**
     * Equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun eq(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return eq(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.NOT_EQUAL, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return ne(getColumnName(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return ne(column, value, true)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return ne(getColumnName(column), value, effective)
    }

    /**
     * Not equal operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ne(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return ne(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.GT, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return gt(getColumnName(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return gt(column, value, true)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return gt(getColumnName(column), value, effective)
    }

    /**
     * Greater than operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun gt(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return gt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.LT, effective)
    }

    /**
     * Less than operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return lt(getColumnName(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return lt(column, value, true)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return lt(getColumnName(column), value, effective)
    }

    /**
     * Less than operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun lt(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return lt(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.GE, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return ge(getColumnName(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return ge(column, value, true)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return ge(getColumnName(column), value, effective)
    }

    /**
     * Greater than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun ge(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return ge(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, value, ComparisonOperator.LE, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return le(getColumnName(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return le(column, value, true)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return le(getColumnName(column), value, effective)
    }

    /**
     * Less than or equal to operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun le(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return le(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "%$value%", ComparisonOperator.LIKE, effective)
    }

    /**
     * Like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return like(getColumnName(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return like(column, value, true)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return like(getColumnName(column), value, effective)
    }

    /**
     * Like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun like(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return like(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "%$value", ComparisonOperator.LIKE, effective)
    }

    /**
     * Left like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return leftLike(getColumnName(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return leftLike(column, value, true)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return leftLike(getColumnName(column), value, effective)
    }

    /**
     * Left like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun leftLike(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return leftLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "$value%", ComparisonOperator.LIKE, effective)
    }

    /**
     * Right like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return rightLike(getColumnName(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return rightLike(column, value, true)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return rightLike(getColumnName(column), value, effective)
    }

    /**
     * Right like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun rightLike(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return rightLike(column, value, true)
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: String, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: String, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: KMutableProperty1<T, *>, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return between(getColumnName(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: KMutableProperty1<T, *>, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return between(column, start, end, true)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: SFunction<T, *>, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return between(getColumnName(column), start, end, effective)
    }

    /**
     * Between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun between(column: SFunction<T, *>, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return between(column, start, end, true)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: String, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: String, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: KMutableProperty1<T, *>, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return `in`(getColumnName(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: KMutableProperty1<T, *>, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: SFunction<T, *>, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return `in`(getColumnName(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: SFunction<T, *>, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: String, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values.toList(), effective)
    }

    /**
     * In operation
     *
     * @param column column name
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: String, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: KMutableProperty1<T, *>, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return `in`(getColumnName(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column property
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: KMutableProperty1<T, *>, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: SFunction<T, *>, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return `in`(getColumnName(column), values, effective)
    }

    /**
     * In operation
     *
     * @param column column function
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun `in`(column: SFunction<T, *>, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return `in`(column, values, true)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "%$value%", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notLike(getColumnName(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLike(column, value, true)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notLike(getColumnName(column), value, effective)
    }

    /**
     * Not like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLike(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "%$value", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notLeftLike(getColumnName(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLeftLike(column, value, true)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notLeftLike(getColumnName(column), value, effective)
    }

    /**
     * Not left like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notLeftLike(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notLeftLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "$value%", ComparisonOperator.NOT_LIKE, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column name
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: String, value: Any): AbstractWhereWrapper<T, R, W> {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notRightLike(getColumnName(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column property
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notRightLike(column, value, true)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notRightLike(getColumnName(column), value, effective)
    }

    /**
     * Not right like operation
     *
     * @param column column function
     * @param value value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notRightLike(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<T, R, W> {
        return notRightLike(column, value, true)
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: String, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, Pair(start, end), ComparisonOperator.NOT_BETWEEN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Not between operation
     *
     * @param column column name
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: String, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: KMutableProperty1<T, *>, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notBetween(getColumnName(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column property
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: KMutableProperty1<T, *>, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return notBetween(column, start, end, true)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: SFunction<T, *>, start: Any, end: Any, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notBetween(getColumnName(column), start, end, effective)
    }

    /**
     * Not between operation
     *
     * @param column column function
     * @param start start value
     * @param end end value
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notBetween(column: SFunction<T, *>, start: Any, end: Any): AbstractWhereWrapper<T, R, W> {
        return notBetween(column, start, end, true)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: String, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(column, values, ComparisonOperator.NOT_IN)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: String, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: KMutableProperty1<T, *>, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notIn(getColumnName(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: KMutableProperty1<T, *>, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: SFunction<T, *>, values: Iterable<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notIn(getColumnName(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: SFunction<T, *>, values: Iterable<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: String, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values.toList(), effective)
    }

    /**
     * Not in operation
     *
     * @param column column name
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: String, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: KMutableProperty1<T, *>, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notIn(getColumnName(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column property
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: KMutableProperty1<T, *>, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: SFunction<T, *>, values: Array<Any>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return notIn(getColumnName(column), values, effective)
    }

    /**
     * Not in operation
     *
     * @param column column function
     * @param values values
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun notIn(column: SFunction<T, *>, values: Array<Any>): AbstractWhereWrapper<T, R, W> {
        return notIn(column, values, true)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: String, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "", ComparisonOperator.IS_NULL, effective)
    }

    /**
     * Is null operation
     *
     * @param column column name
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: String): AbstractWhereWrapper<T, R, W> {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: KMutableProperty1<T, *>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return isNull(getColumnName(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column property
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: KMutableProperty1<T, *>): AbstractWhereWrapper<T, R, W> {
        return isNull(column, true)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: SFunction<T, *>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return isNull(getColumnName(column), effective)
    }

    /**
     * Is null operation
     *
     * @param column column function
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNull(column: SFunction<T, *>): AbstractWhereWrapper<T, R, W> {
        return isNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: String, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return compare(column, "", ComparisonOperator.IS_NOT_NULL, effective)
    }

    /**
     * Is not null operation
     *
     * @param column column name
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: String): AbstractWhereWrapper<T, R, W> {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: KMutableProperty1<T, *>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return isNotNull(getColumnName(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column property
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: KMutableProperty1<T, *>): AbstractWhereWrapper<T, R, W> {
        return isNotNull(column, true)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @param effective whether effective
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: SFunction<T, *>, effective: Boolean): AbstractWhereWrapper<T, R, W> {
        return isNotNull(getColumnName(column), effective)
    }

    /**
     * Is not null operation
     *
     * @param column column function
     * @return AbstractWhereWrapper<T, R, W>
     */
    fun isNotNull(column: SFunction<T, *>): AbstractWhereWrapper<T, R, W> {
        return isNotNull(column, true)
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    fun groupBy(vararg columns: String): WhereGroupByWrapper<T, R, W> {
        whereGroupByWrapper = WhereGroupByWrapper(wrapper, this, columns.toMutableList())
        return whereGroupByWrapper
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    @SafeVarargs
    fun groupBy(vararg columns: KMutableProperty1<T, *>): WhereGroupByWrapper<T, R, W> {
        return groupBy(*columns.map { getColumnName(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    @SafeVarargs
    fun groupBy(vararg columns: SFunction<T, *>): WhereGroupByWrapper<T, R, W> {
        return groupBy(*columns.map { getColumnName(it) }.toTypedArray())
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return WhereOrderByWrapper<T, R, W>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<T>): WhereOrderByWrapper<T, R, W> {
        whereOrderByWrapper = WhereOrderByWrapper(wrapper, this, orderBys.toMutableList())
        return whereOrderByWrapper
    }

    /**
     * Order by operation
     *
     * @param column column name
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: String, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column property
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: KMutableProperty1<T, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: SFunction<T, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem<T>(column, asc))
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
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

    open fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        if (conditions.isNotEmpty()) {
            sql.append(WHERE)
            if (conditions.last().nestedConditions.isEmpty()) {
                conditions.last().logicalOperator = null
            }
            conditions.forEach {
                it.appendSql(sql, parameters)
            }
        }
        if (isGroupByInitialized()) {
            whereGroupByWrapper.appendSql(sql)
        }
        if (isHavingInitialized()) {
            whereHavingWrapper.appendSql(sql, parameters)
        }
        if (isOrderByInitialized()) {
            whereOrderByWrapper.appendSql(sql)
        }
    }

}
