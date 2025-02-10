package com.tang.kite.wrapper.where

import com.tang.kite.constants.SqlString.ASC
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.DESC
import com.tang.kite.constants.SqlString.ORDER_BY
import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Wrapper
import kotlin.reflect.KMutableProperty1

/**
 * Where order by wrapper
 *
 * @author Tang
 */
class WhereOrderByWrapper<T, R, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: AbstractWhereWrapper<T, R, W>,

    private val columns: MutableList<OrderItem<*>> = mutableListOf()

): WhereBuilder<T, R, W> {

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return WhereOrderByWrapper<T, R, W>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<*>): WhereOrderByWrapper<T, R, W> {
        columns.addAll(orderBys)
        return this
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
     * @param column column name
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: String): WhereOrderByWrapper<T, R, W> {
        return orderBy(column, true)
    }

    /**
     * Order by operation
     *
     * @param column column property
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: KMutableProperty1<*, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return WhereOrderByWrapper<T, R, W>
     */
    fun orderBy(column: SFunction<*, *>, asc: Boolean = true): WhereOrderByWrapper<T, R, W> {
        return orderBy(OrderItem(column, asc))
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

    fun appendSql(sql: StringBuilder, multiTableQuery: Boolean) {
        if (columns.isEmpty()) {
            return
        }
        sql.append(ORDER_BY)
        columns.joinToString(COMMA_SPACE) {
            it.column.toString(multiTableQuery) + if (it.asc) ASC else DESC
        }.let {
            sql.append(it)
        }
    }

}
