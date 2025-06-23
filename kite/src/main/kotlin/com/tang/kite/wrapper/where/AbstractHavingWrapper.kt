package com.tang.kite.wrapper.where

import com.tang.kite.constants.SqlString.HAVING
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Fields
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.enumeration.ComparisonOperator
import com.tang.kite.wrapper.enumeration.LogicalOperator
import com.tang.kite.wrapper.statement.ComparisonStatement
import com.tang.kite.wrapper.statement.LogicalStatement
import java.lang.reflect.Field
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
abstract class AbstractHavingWrapper<R, T>(

    private val wrapper: Wrapper<T>,

    private val conditions: MutableList<LogicalStatement> = mutableListOf()

): AbstractConditionWrapper<AbstractHavingWrapper<R, T>, T>(conditions) {

    init {
        this.conditionInstance = this
    }

    private fun getColumnName(field: Field): String {
        return Reflects.getColumnName(field, true)
    }

    private fun getColumnName(column: KMutableProperty1<T, *>): String {
        val field = column.javaField!!
        return getColumnName(field)
    }

    private fun getColumnName(column: SFunction<T, *>): String {
        val field = Fields.getField(column)
        return getColumnName(field)
    }

    /**
     * And operation
     *
     * @return AbstractHavingWrapper<R, T>
     */
    fun and(): AbstractHavingWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.AND)
        return this
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return AbstractHavingWrapper<R, T>
     */
    fun and(nested: AbstractHavingWrapper<R, T>.() -> Unit): AbstractHavingWrapper<R, T> {
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
     * @return AbstractHavingWrapper<R, T>
     */
    fun and(nested: Consumer<AbstractHavingWrapper<R, T>>): AbstractHavingWrapper<R, T> {
        return and { nested.accept(this) }
    }

    /**
     * Or operation
     *
     * @return AbstractHavingWrapper<R, T>
     */
    fun or(): AbstractHavingWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.OR)
        return this
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return AbstractHavingWrapper<R, T>
     */
    fun or(nested: AbstractHavingWrapper<R, T>.() -> Unit): AbstractHavingWrapper<R, T> {
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
     * @return AbstractHavingWrapper<R, T>
     */
    fun or(nested: Consumer<AbstractHavingWrapper<R, T>>): AbstractHavingWrapper<R, T> {
        return or { nested.accept(this) }
    }

    /**
     * And not operation
     *
     * @return AbstractHavingWrapper<R, T>
     */
    fun andNot(): AbstractHavingWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.AND_NOT)
        return this
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return AbstractHavingWrapper<R, T>
     */
    fun andNot(nested: AbstractHavingWrapper<R, T>.() -> Unit): AbstractHavingWrapper<R, T> {
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
     * @return AbstractHavingWrapper<R, T>
     */
    fun andNot(nested: Consumer<AbstractHavingWrapper<R, T>>): AbstractHavingWrapper<R, T> {
        return andNot { nested.accept(this) }
    }

    /**
     * Or not operation
     *
     * @return AbstractHavingWrapper<R, T>
     */
    fun orNot(): AbstractHavingWrapper<R, T> {
        setLastLogicalOperator(LogicalOperator.OR_NOT)
        return this
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return AbstractHavingWrapper<R, T>
     */
    fun orNot(nested: AbstractHavingWrapper<R, T>.() -> Unit): AbstractHavingWrapper<R, T> {
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
     * @return AbstractHavingWrapper<R, T>
     */
    fun orNot(nested: Consumer<AbstractHavingWrapper<R, T>>): AbstractHavingWrapper<R, T> {
        return orNot { nested.accept(this) }
    }

    private fun createNestedWrapper(): AbstractHavingWrapper<R, T> {
        val wrapper = WrapperBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractHavingWrapper<R, T>
    }

    private fun compare(column: String, value: Any, comparisonOperator: ComparisonOperator, effective: Boolean): AbstractHavingWrapper<R, T> {
        if (effective) {
            val condition = ComparisonStatement(Column(column), value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): Wrapper<T> {
        return wrapper
    }

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, multiTableQuery: Boolean) {
        if (conditions.isEmpty()) {
            return
        }
        sql.append(HAVING)
        if (conditions.last().nestedConditions.isEmpty()) {
            conditions.last().logicalOperator = null
        }
        conditions.forEach {
            it.appendSql(sql, parameters, multiTableQuery)
        }
    }

}
