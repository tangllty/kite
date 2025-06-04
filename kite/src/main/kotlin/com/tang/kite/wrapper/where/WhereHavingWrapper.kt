package com.tang.kite.wrapper.where

import com.tang.kite.constants.SqlString.HAVING
import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
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
class WhereHavingWrapper<T, R, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: AbstractWhereWrapper<T, R, W>,

    private val conditions: MutableList<LogicalStatement> = mutableListOf()

): AbstractConditionWrapper<WhereHavingWrapper<T, R, W>, T, R, W>(conditions) {

    init {
        this.rtInstance = this
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
     * @return WhereHavingWrapper<T, R, W>
     */
    fun and(): WhereHavingWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.AND)
        return this
    }

    /**
     * And nested operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun and(nested: WhereHavingWrapper<T, R, W>.() -> Unit): WhereHavingWrapper<T, R, W> {
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
     * @return WhereHavingWrapper<T, R, W>
     */
    fun and(nested: Consumer<WhereHavingWrapper<T, R, W>>): WhereHavingWrapper<T, R, W> {
        return and { nested.accept(this) }
    }

    /**
     * Or operation
     *
     * @return WhereHavingWrapper<T, R, W>
     */
    fun or(): WhereHavingWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.OR)
        return this
    }

    /**
     * Or nested operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun or(nested: WhereHavingWrapper<T, R, W>.() -> Unit): WhereHavingWrapper<T, R, W> {
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
     * @return WhereHavingWrapper<T, R, W>
     */
    fun or(nested: Consumer<WhereHavingWrapper<T, R, W>>): WhereHavingWrapper<T, R, W> {
        return or { nested.accept(this) }
    }

    /**
     * And not operation
     *
     * @return WhereHavingWrapper<T, R, W>
     */
    fun andNot(): WhereHavingWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.AND_NOT)
        return this
    }

    /**
     * And not nested operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun andNot(nested: WhereHavingWrapper<T, R, W>.() -> Unit): WhereHavingWrapper<T, R, W> {
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
     * @return WhereHavingWrapper<T, R, W>
     */
    fun andNot(nested: Consumer<WhereHavingWrapper<T, R, W>>): WhereHavingWrapper<T, R, W> {
        return andNot { nested.accept(this) }
    }

    /**
     * Or not operation
     *
     * @return WhereHavingWrapper<T, R, W>
     */
    fun orNot(): WhereHavingWrapper<T, R, W> {
        setLastLogicalOperator(LogicalOperator.OR_NOT)
        return this
    }

    /**
     * Or not nested operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun orNot(nested: WhereHavingWrapper<T, R, W>.() -> Unit): WhereHavingWrapper<T, R, W> {
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
     * @return WhereHavingWrapper<T, R, W>
     */
    fun orNot(nested: Consumer<WhereHavingWrapper<T, R, W>>): WhereHavingWrapper<T, R, W> {
        return orNot { nested.accept(this) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createNestedWrapper(): WhereHavingWrapper<T, R, W> {
        val wrapper = WhereBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as WhereHavingWrapper<T, R, W>
    }

    private fun compare(column: String, value: Any, comparisonOperator: ComparisonOperator, effective: Boolean): WhereHavingWrapper<T, R, W> {
        if (effective) {
            val condition = ComparisonStatement(Column(column), value, comparisonOperator)
            conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        }
        return this
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return WhereOrderByWrapper<T, R, W>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<T>): WhereOrderByWrapper<T, R, W> {
        whereWrapper.whereOrderByWrapper = WhereOrderByWrapper(wrapper, whereWrapper, orderBys.toMutableList())
        return whereWrapper.whereOrderByWrapper
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
     * Order by ascending with column name
     *
     * @param column column name
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByAsc(column: String): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with column name
     *
     * @param column column name
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByDesc(column: String): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with property reference
     *
     * @param column column property
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByAsc(column: KMutableProperty1<T, *>): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with property reference
     *
     * @param column column property
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByDesc(column: KMutableProperty1<T, *>): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with SFunction
     *
     * @param column column function
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByAsc(column: SFunction<T, *>): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with SFunction
     *
     * @param column column function
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderByDesc(column: SFunction<T, *>): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, false)
    }

    /**
     * Build the wrapper
     *
     * @return W
     */
    @Suppress("UNCHECKED_CAST")
    override fun build(): W {
        return wrapper as W
    }

    /**
     * Execute the wrapper
     *
     * @return R
     */
    override fun execute(): R {
        return whereWrapper.execute()
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
