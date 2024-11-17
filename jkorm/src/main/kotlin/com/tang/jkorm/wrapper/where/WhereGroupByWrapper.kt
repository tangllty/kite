package com.tang.jkorm.wrapper.where

import com.tang.jkorm.constants.SqlString.COMMA_SPACE
import com.tang.jkorm.constants.SqlString.GROUP_BY
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Reflects.getColumnName
import com.tang.jkorm.wrapper.Wrapper
import kotlin.reflect.KMutableProperty1

/**
 * Where group by wrapper
 *
 * @author Tang
 */
class WhereGroupByWrapper<T, R, W>(

    private val wrapper: Wrapper<T>,

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
        return (wrapper as AbstractWhereWrapper<T, R, W>).execute()
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
