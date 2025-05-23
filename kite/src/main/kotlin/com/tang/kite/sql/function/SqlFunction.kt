package com.tang.kite.sql.function

import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects.getColumnName
import kotlin.reflect.KMutableProperty1

/**
 * Static factory methods for SQL functions.
 *
 * @author Tang
 */
object SqlFunction {

    private const val AVG = "AVG"
    private const val COUNT = "COUNT"
    private const val LENGTH = "LENGTH"
    private const val LOWER = "LOWER"
    private const val MAX = "MAX"
    private const val MIN = "MIN"
    private const val SUM = "SUM"
    private const val UPPER = "UPPER"
    private const val STAR = "*"

    private fun wrap(function: String, column: String): FunctionColumn {
        return FunctionColumn(function, column)
    }

    private fun <T> wrap(function: String, property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(function, getColumnName(property))
    }

    private fun <T> wrap(function: String, field: SFunction<T, *>): FunctionColumn {
        return wrap(function, getColumnName(field))
    }

    @JvmStatic
    fun avg(column: String): FunctionColumn {
        return wrap(AVG, column)
    }

    @JvmStatic
    fun <T> avg(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(AVG, property)
    }

    @JvmStatic
    fun <T> avg(field: SFunction<T, *>): FunctionColumn {
        return wrap(AVG, field)
    }

    @JvmStatic
    fun count(column: String = STAR): FunctionColumn {
        return wrap(COUNT, column)
    }

    @JvmStatic
    fun <T> count(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(COUNT, property)
    }

    @JvmStatic
    fun <T> count(field: SFunction<T, *>): FunctionColumn {
        return wrap(COUNT, field)
    }

    @JvmStatic
    fun length(column: String): FunctionColumn {
        return wrap(LENGTH, column)
    }

    @JvmStatic
    fun <T> length(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(LENGTH, property)
    }

    @JvmStatic
    fun <T> length(field: SFunction<T, *>): FunctionColumn {
        return wrap(LENGTH, field)
    }

    @JvmStatic
    fun lower(column: String): FunctionColumn {
        return wrap(LOWER, column)
    }

    @JvmStatic
    fun <T> lower(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(LOWER, property)
    }

    @JvmStatic
    fun <T> lower(field: SFunction<T, *>): FunctionColumn {
        return wrap(LOWER, field)
    }

    @JvmStatic
    fun max(column: String): FunctionColumn {
        return wrap(MAX, column)
    }

    @JvmStatic
    fun <T> max(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(MAX, property)
    }

    @JvmStatic
    fun <T> max(field: SFunction<T, *>): FunctionColumn {
        return wrap(MAX, field)
    }

    @JvmStatic
    fun min(column: String): FunctionColumn {
        return wrap(MIN, column)
    }

    @JvmStatic
    fun <T> min(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(MIN, property)
    }

    @JvmStatic
    fun <T> min(field: SFunction<T, *>): FunctionColumn {
        return wrap(MIN, field)
    }

    @JvmStatic
    fun sum(column: String): FunctionColumn {
        return wrap(SUM, column)
    }

    @JvmStatic
    fun <T> sum(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(SUM, property)
    }

    @JvmStatic
    fun <T> sum(field: SFunction<T, *>): FunctionColumn {
        return wrap(SUM, field)
    }

    @JvmStatic
    fun upper(column: String): FunctionColumn {
        return wrap(UPPER, column)
    }

    @JvmStatic
    fun <T> upper(property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(UPPER, property)
    }

    @JvmStatic
    fun <T> upper(field: SFunction<T, *>): FunctionColumn {
        return wrap(UPPER, field)
    }

}
