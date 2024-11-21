package com.tang.jkorm.wrapper.where

import com.tang.jkorm.constants.SqlString.COMMA_SPACE
import com.tang.jkorm.constants.SqlString.GROUP_BY
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.paginate.OrderItem
import com.tang.jkorm.utils.Reflects.getColumnName
import com.tang.jkorm.wrapper.Wrapper
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty1

/**
 * Where group by wrapper
 *
 * @author Tang
 */
class WhereGroupByWrapper<T, R, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: AbstractWhereWrapper<T, R, W>,

    private val columns: MutableList<String> = mutableListOf()

): WhereBuilder<T, R, W> {

    /**
     * Group by operation
     *
     * @param columns columns
     * @return WhereGroupByWrapper<T, R, W>
     */
    fun groupBy(vararg columns: String): WhereGroupByWrapper<T, R, W> {
        this.columns.addAll(columns)
        return this
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
     * Having operation
     *
     * @return WhereHavingWrapper<T, R, W>
     */
    fun having(): WhereHavingWrapper<T, R, W> {
        whereWrapper.whereHavingWrapper = WhereHavingWrapper(wrapper, whereWrapper)
        return whereWrapper.whereHavingWrapper
    }

    /**
     * Having operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun having(nested: WhereHavingWrapper<T, R, W>.() -> Unit): WhereHavingWrapper<T, R, W> {
        whereWrapper.whereHavingWrapper = WhereHavingWrapper(wrapper, whereWrapper)
        whereWrapper.whereHavingWrapper.nested()
        return whereWrapper.whereHavingWrapper
    }

    /**
     * Having operation
     *
     * @param nested nested operation
     * @return WhereHavingWrapper<T, R, W>
     */
    fun having(nested: Consumer<WhereHavingWrapper<T, R, W>>): WhereHavingWrapper<T, R, W> {
        return having { nested.accept(this) }
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return WhereOrderByWrapper<T, R, W>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<T>): WhereOrderByWrapper<T, R, W> {
        return whereWrapper.orderBy(*orderBys)
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
    @Suppress("UNCHECKED_CAST")
    override fun execute(): R {
        return whereWrapper.execute()
    }

    fun appendSql(sql: StringBuilder) {
        if (columns.isEmpty()) {
            return
        }
        sql.append(GROUP_BY)
        columns.joinToString(COMMA_SPACE).let {
            sql.append(it)
        }
    }

}
