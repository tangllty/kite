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

    private fun wrap(function: String, column: String?): FunctionColumn {
        return FunctionColumn(function, column)
    }

    private fun <T> wrap(function: String, property: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(function, getColumnName(property))
    }

    private fun <T> wrap(function: String, field: SFunction<T, *>): FunctionColumn {
        return wrap(function, getColumnName(field))
    }

    @JvmStatic
    fun avg(value: String?): FunctionColumn {
        return wrap(AVG, value)
    }

    @JvmStatic
    fun <T> avg(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(AVG, column)
    }

    @JvmStatic
    fun <T> avg(column: SFunction<T, *>): FunctionColumn {
        return wrap(AVG, column)
    }

    @JvmStatic
    fun count(value: String? = STAR): FunctionColumn {
        return wrap(COUNT, value)
    }

    @JvmStatic
    fun <T> count(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(COUNT, column)
    }

    @JvmStatic
    fun <T> count(column: SFunction<T, *>): FunctionColumn {
        return wrap(COUNT, column)
    }

    @JvmStatic
    fun length(value: String?): FunctionColumn {
        return wrap(LENGTH, value)
    }

    @JvmStatic
    fun <T> length(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(LENGTH, column)
    }

    @JvmStatic
    fun <T> length(column: SFunction<T, *>): FunctionColumn {
        return wrap(LENGTH, column)
    }

    @JvmStatic
    fun lower(value: String?): FunctionColumn {
        return wrap(LOWER, value)
    }

    @JvmStatic
    fun <T> lower(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(LOWER, column)
    }

    @JvmStatic
    fun <T> lower(column: SFunction<T, *>): FunctionColumn {
        return wrap(LOWER, column)
    }

    @JvmStatic
    fun max(value: String?): FunctionColumn {
        return wrap(MAX, value)
    }

    @JvmStatic
    fun <T> max(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(MAX, column)
    }

    @JvmStatic
    fun <T> max(column: SFunction<T, *>): FunctionColumn {
        return wrap(MAX, column)
    }

    @JvmStatic
    fun min(value: String?): FunctionColumn {
        return wrap(MIN, value)
    }

    @JvmStatic
    fun <T> min(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(MIN, column)
    }

    @JvmStatic
    fun <T> min(column: SFunction<T, *>): FunctionColumn {
        return wrap(MIN, column)
    }

    @JvmStatic
    fun sum(value: String?): FunctionColumn {
        return wrap(SUM, value)
    }

    @JvmStatic
    fun <T> sum(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(SUM, column)
    }

    @JvmStatic
    fun <T> sum(column: SFunction<T, *>): FunctionColumn {
        return wrap(SUM, column)
    }

    @JvmStatic
    fun upper(value: String?): FunctionColumn {
        return wrap(UPPER, value)
    }

    @JvmStatic
    fun <T> upper(column: KMutableProperty1<T, *>): FunctionColumn {
        return wrap(UPPER, column)
    }

    @JvmStatic
    fun <T> upper(column: SFunction<T, *>): FunctionColumn {
        return wrap(UPPER, column)
    }

}
