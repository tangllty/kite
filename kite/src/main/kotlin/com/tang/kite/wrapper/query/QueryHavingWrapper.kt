package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractHavingWrapper
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
class QueryHavingWrapper<R, T, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: QueryWhereWrapper<T>,

    conditions: MutableList<LogicalStatement> = mutableListOf()

) : AbstractHavingWrapper<QueryHavingWrapper<R, T, W>, T, W>(wrapper, conditions), QueryBuilder<T> {

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<T>): QueryOrderByWrapper<QueryWhereWrapper<T>, T, QueryWrapper<T>> {
        whereWrapper.whereOrderByWrapper = QueryOrderByWrapper(wrapper, whereWrapper, orderBys.toMutableList())
        return whereWrapper.whereOrderByWrapper
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
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        return whereWrapper.build().baseMapper.selectWrapper(wrapper as QueryWrapper<T>).toMutableList()
    }

}
