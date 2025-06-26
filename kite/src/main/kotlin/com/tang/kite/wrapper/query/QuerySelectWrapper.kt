package com.tang.kite.wrapper.query

import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.FROM
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects
import com.tang.kite.wrapper.Column
import kotlin.reflect.KMutableProperty1

/**
 * Select statement wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QuerySelectWrapper<T>(

    private val queryWrapper: QueryWrapper<T>,

    private val columns: MutableList<Column>

) {

    private lateinit var tableClass: Class<*>

    private lateinit var table: String

    /**
     * Set the columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun columns(vararg columns: Column): QuerySelectWrapper<T> {
        this.columns.addAll(columns)
        return this
    }

    /**
     * Set the column
     *
     * @param column column
     * @return QuerySelectWrapper
     */
    fun column(column: Column): QuerySelectWrapper<T> {
        this.columns.add(column)
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
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): QueryWhereWrapper<T> {
        this.table = table
        this.queryWrapper.queryWhereWrapper = QueryWhereWrapper(queryWrapper)
        return queryWrapper.queryWhereWrapper
    }

    /**
     * Set the table name by class
     *
     * @param clazz entity class
     */
    fun <E> from(clazz: Class<E>): QueryWhereWrapper<T> {
        this.tableClass = clazz
        return from(Reflects.getTableName(clazz))
    }

    /**
     * Append the SQL
     *
     * @param sql SQL
     */
    fun appendSql(sql: StringBuilder, joinedClass: List<Class<*>>, isMultiTableQuery: Boolean) {
        checkValues()
        if (columns.isEmpty()) {
            listOf(tableClass).plus(joinedClass).forEach {
                val fields = Reflects.getSqlFields(it)
                fields.forEach { field ->
                    columns.add(Column(field))
                }
            }
        }
        sql.append(columns.joinToString(COMMA_SPACE) { it.toString(isMultiTableQuery) })
        sql.append(FROM + table)
        if (isMultiTableQuery) {
            val tableAlias = Reflects.getTableAlias(tableClass)
            sql.append(" $tableAlias")
        }
    }

    /**
     * Check the values
     */
    private fun checkValues() {
    }

}
