package com.tang.kite.wrapper.where

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
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

) : WrapperBuilder<T> {

    protected var groupByInstance: R? = null

    private fun getInstance(): R {
        return groupByInstance ?: throw IllegalStateException("GroupBy instance is not initialized")
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return R
     */
    fun groupBy(vararg columns: Column): R {
        this.columns.addAll(columns)
        return getInstance()
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

    fun appendSqlNode(orderBy: MutableList<Column>) {
        orderBy.addAll(columns)
    }

}
