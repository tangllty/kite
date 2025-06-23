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
abstract class AbstractOrderByWrapper<R, T, W>(

    private val wrapper: Wrapper<T>,

    private val columns: MutableList<OrderItem<*>> = mutableListOf()

): WrapperBuilder<T, W> {


    @Suppress("UNCHECKED_CAST")
    protected var orderByInstance: R = Any() as R

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return R
     */
    @SafeVarargs
    fun <E> orderBy(vararg orderBys: OrderItem<E>): R {
        columns.addAll(orderBys)
        return orderByInstance
    }

    /**
     * Order by operation
     *
     * @param column column name
     * @param asc asc or desc
     * @return R
     */
    fun orderBy(column: String, asc: Boolean = true): R {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column name
     * @return R
     */
    fun orderBy(column: String): R {
        return orderBy(column, true)
    }

    /**
     * Order by operation
     *
     * @param column column property
     * @param asc asc or desc
     * @return R
     */
    fun <E> orderBy(column: KMutableProperty1<E, *>, asc: Boolean = true): R {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return R
     */
    fun <E> orderBy(column: SFunction<E, *>, asc: Boolean = true): R {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by ascending with column name
     *
     * @param column column name
     * @return R
     */
    fun orderByAsc(column: String): R {
        return orderBy(column, true)
    }

    /**
     * Order by descending with column name
     *
     * @param column column name
     * @return R
     */
    fun orderByDesc(column: String): R {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with property reference
     *
     * @param column column property
     * @return R
     */
    fun <E> orderByAsc(column: KMutableProperty1<E, *>): R {
        return orderBy(column, true)
    }

    /**
     * Order by descending with property reference
     *
     * @param column column property
     * @return R
     */
    fun <E> orderByDesc(column: KMutableProperty1<E, *>): R {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with SFunction
     *
     * @param column column function
     * @return R
     */
    fun <E> orderByAsc(column: SFunction<E, *>): R {
        return orderBy(column, true)
    }

    /**
     * Order by descending with SFunction
     *
     * @param column column function
     * @return R
     */
    fun <E> orderByDesc(column: SFunction<E, *>): R {
        return orderBy(column, false)
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    @Suppress("UNCHECKED_CAST")
    override fun build(): W {
        return wrapper as W
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
