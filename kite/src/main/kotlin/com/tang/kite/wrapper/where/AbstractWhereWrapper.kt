package com.tang.kite.wrapper.where

import com.tang.kite.config.KiteConfig
import com.tang.kite.constants.SqlString.WHERE
import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.SqlStatement
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.enumeration.LogicalOperator
import com.tang.kite.wrapper.statement.LogicalStatement
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

) : AbstractConditionWrapper<AbstractWhereWrapper<T, R, W>, T, R, W>(conditions) {

    protected lateinit var whereGroupByWrapper: WhereGroupByWrapper<T, R, W>

    lateinit var whereHavingWrapper: WhereHavingWrapper<T, R, W>

    lateinit var whereOrderByWrapper: WhereOrderByWrapper<T, R, W>

    init {
        this.rtInstance = this
    }

    protected fun isGroupByInitialized(): Boolean {
        return this::whereGroupByWrapper.isInitialized
    }

    protected fun isHavingInitialized(): Boolean {
        return this::whereHavingWrapper.isInitialized
    }

    protected fun isOrderByInitialized(): Boolean {
        return this::whereOrderByWrapper.isInitialized
    }

    @Suppress("UNCHECKED_CAST")
    private fun createNestedWrapper(): AbstractWhereWrapper<T, R, W> {
        val wrapper = WhereBuilder::class.java.getMethod("build").invoke(this)
        val firstConstructor = this.javaClass.constructors.first()
        return firstConstructor.newInstance(wrapper) as AbstractWhereWrapper<T, R, W>
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

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    fun groupBy(vararg columns: Column): WhereGroupByWrapper<T, R, W> {
        whereGroupByWrapper = WhereGroupByWrapper(wrapper, this, columns.toMutableList())
        return whereGroupByWrapper
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    fun groupBy(vararg columns: String): WhereGroupByWrapper<T, R, W> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    @SafeVarargs
    fun <E> groupBy(vararg columns: KMutableProperty1<E, *>): WhereGroupByWrapper<T, R, W> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    @SafeVarargs
    fun <E> groupBy(vararg columns: SFunction<E, *>): WhereGroupByWrapper<T, R, W> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return WhereOrderByWrapper<T, R, W>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<*>): WhereOrderByWrapper<T, R, W> {
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
    fun <E> orderBy(column: KMutableProperty1<E, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun <E> orderBy(column: SFunction<E, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun <E> orderBy(column: SFunction<E, *>): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, true)
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
        return SqlStatement(KiteConfig.getSql(sql), parameters)
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
        if (isGroupByInitialized()) {
            whereGroupByWrapper.appendSql(sql, multiTableQuery)
        }
        if (isHavingInitialized()) {
            whereHavingWrapper.appendSql(sql, parameters, multiTableQuery)
        }
        if (isOrderByInitialized()) {
            whereOrderByWrapper.appendSql(sql, multiTableQuery)
        }
    }

}
