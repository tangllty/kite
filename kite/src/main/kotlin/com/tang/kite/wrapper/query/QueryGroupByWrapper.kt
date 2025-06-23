package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.where.AbstractGroupByWrapper
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
class QueryGroupByWrapper<R, T, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: QueryWhereWrapper<T>,

    columns: MutableList<Column> = mutableListOf()

) : AbstractGroupByWrapper<QueryGroupByWrapper<R, T, W>, T, W>(wrapper, columns), QueryBuilder<T> {

    init {
        this.groupByInstance = this
    }

    /**
     * Having operation
     *
     * @return QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun having(): QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        whereWrapper.whereHavingWrapper = QueryHavingWrapper(wrapper, whereWrapper, mutableListOf())
        return whereWrapper.whereHavingWrapper
    }

    /**
     * Having operation
     *
     * @param nested nested operation
     * @return QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun having(nested: QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>.() -> Unit): QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        whereWrapper.whereHavingWrapper = QueryHavingWrapper(wrapper, whereWrapper)
        whereWrapper.whereHavingWrapper.nested()
        return whereWrapper.whereHavingWrapper
    }

    /**
     * Having operation
     *
     * @param nested nested operation
     * @return QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun having(nested: Consumer<QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>>): QueryHavingWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return having { nested.accept(this) }
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<T>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return whereWrapper.orderBy(*orderBys)
    }

    /**
     * Order by operation
     *
     * @param column column name
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderBy(column: String, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column property
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderBy(column: KMutableProperty1<T, *>, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderBy(column: SFunction<T, *>, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by ascending with column name
     *
     * @param column column name
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByAsc(column: String): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with column name
     *
     * @param column column name
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByDesc(column: String): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with property reference
     *
     * @param column column property
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByAsc(column: KMutableProperty1<T, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with property reference
     *
     * @param column column property
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByDesc(column: KMutableProperty1<T, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with SFunction
     *
     * @param column column function
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByAsc(column: SFunction<T, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with SFunction
     *
     * @param column column function
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    fun orderByDesc(column: SFunction<T, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        return orderBy(column, false)
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    @Suppress("UNCHECKED_CAST")
    override fun build(): W {
        return whereWrapper.build() as W
    }

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        return whereWrapper.build().baseMapper.selectWrapper(wrapper as QueryWrapper<T>).toMutableList()
    }

}
