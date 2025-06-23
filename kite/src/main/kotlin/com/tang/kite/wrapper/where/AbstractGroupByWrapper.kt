package com.tang.kite.wrapper.where

import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.GROUP_BY
import com.tang.kite.function.SFunction
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.Wrapper
import kotlin.reflect.KMutableProperty1

/**
 * Where group by wrapper
 *
 * @author Tang
 */
abstract class AbstractGroupByWrapper<R, T>(

    private val wrapper: Wrapper<T>,

    private val columns: MutableList<Column> = mutableListOf()

): WrapperBuilder<T> {

    @Suppress("UNCHECKED_CAST")
    protected var groupByInstance: R = Any() as R

    /**
     * Group by operation
     *
     * @param columns columns
     * @return R
     */
    fun groupBy(vararg columns: Column): R {
        this.columns.addAll(columns)
        return groupByInstance
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return R
     */
    fun groupBy(vararg columns: String): R {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return R
     */
    @SafeVarargs
    fun groupBy(vararg columns: KMutableProperty1<T, *>): R {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return R
     */
    @SafeVarargs
    fun groupBy(vararg columns: SFunction<T, *>): R {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): Wrapper<T> {
        return wrapper
    }

    fun appendSql(sql: StringBuilder, multiTableQuery: Boolean) {
        if (columns.isEmpty()) {
            return
        }
        sql.append(GROUP_BY)
        val orderBys = columns.joinToString(COMMA_SPACE) { it.toString(multiTableQuery) }
        sql.append(orderBys)
    }

}
