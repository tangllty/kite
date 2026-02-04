package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.TableReference
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

/**
 * Select statement wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QuerySelectWrapper<T : Any>(

    private val queryWrapper: QueryWrapper<T>,

    private val sqlNode: SqlNode.Select

) {

    /**
     * Set the columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun columns(vararg columns: Column): QuerySelectWrapper<T> {
        sqlNode.columns.addAll(columns)
        return this
    }

    /**
     * Set the column
     *
     * @param column column
     * @return QuerySelectWrapper
     */
    fun column(column: Column): QuerySelectWrapper<T> {
        sqlNode.columns.add(column)
        return this
    }

    /**
     * Set the columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun columns(vararg columns: String): QuerySelectWrapper<T> {
        return columns(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Set the column
     *
     * @param column column name
     * @return QuerySelectWrapper
     */
    fun column(column: String): QuerySelectWrapper<T> {
        return column(Column(column))
    }

    /**
     * Set the columns
     *
     * @param columns columns properties
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun <E> columns(vararg columns: KMutableProperty1<E, *>): QuerySelectWrapper<T> {
        return columns(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Set the column
     *
     * @param column column property
     * @return QuerySelectWrapper
     */
    fun <E> column(column: KMutableProperty1<E, *>): QuerySelectWrapper<T> {
        return column(Column(column))
    }

    /**
     * Set the columns
     *
     * @param columns columns properties
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun <E> columns(vararg columns: SFunction<E, *>): QuerySelectWrapper<T> {
        return columns(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Set the column
     *
     * @param column column property
     * @return QuerySelectWrapper
     */
    fun <E> column(column: SFunction<E, *>): QuerySelectWrapper<T> {
        return column(Column(column))
    }

    /**
     * Set the table reference
     *
     * @param tableReference Table reference
     */
    fun from(tableReference: TableReference): QueryWhereWrapper<T> {
        sqlNode.from = tableReference
        this.queryWrapper.queryWhereWrapper = QueryWhereWrapper(queryWrapper, sqlNode.where)
        return queryWrapper.queryWhereWrapper
    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): QueryWhereWrapper<T> {
        return from(TableReference(table))
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: Class<T>): QueryWhereWrapper<T> {
        return from(TableReference(clazz))
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun from(clazz: KClass<T>): QueryWhereWrapper<T> {
        return from(TableReference(clazz))
    }

}
