package com.tang.jkorm.wrapper.query

import com.tang.jkorm.constants.SqlString.FROM
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
class QuerySelectWrapper<T>(

    private val queryWrapper: QueryWrapper<T>,

    private val columns: MutableList<String>

) {

    private lateinit var tableClass: Class<*>

    private lateinit var table: String

    fun columns(vararg columns: String): QuerySelectWrapper<T> {
        this.columns.addAll(columns.toList())
        return this
    }

    fun column(column: String): QuerySelectWrapper<T> {
        this.columns.add(column)
        return this
    }

    fun columns(vararg columns: SFunction<T, *>): QuerySelectWrapper<T> {
        val columnNames = columns.map { Reflects.getColumnName(Fields.getField(it)) }
        return columns(*columnNames.toTypedArray())
    }

    fun column(column: SFunction<T, *>): QuerySelectWrapper<T> {
        return column(Reflects.getColumnName(Fields.getField(column)))
    }

    fun columns(vararg columns: KMutableProperty1<T, *>): QuerySelectWrapper<T> {
        val columnNames = columns.map { Reflects.getColumnName(it.javaField!!) }
        return columns(*columnNames.toTypedArray())
    }

    fun column(column: KMutableProperty1<T, *>): QuerySelectWrapper<T> {
        return column(Reflects.getColumnName(column.javaField!!))
    }

    /**
     * Set the table name
     *
     * @param table table name
     */
    fun from(table: String): QueryWhereWrapper<T> {
        this.table = table
        this.queryWrapper.queryWhereWrapper = QueryWhereWrapper<T>(queryWrapper)
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

    fun appendSql(sql: StringBuilder) {
        checkValues()
        if (columns.isEmpty()) {
            tableClass.declaredFields.forEach {
                columns.add(Reflects.getColumnName(it))
            }
        }
        sql.append(columns.joinToString(", "))
        sql.append(FROM)
        sql.append(table)
    }

    private fun checkValues() {
    }

}
