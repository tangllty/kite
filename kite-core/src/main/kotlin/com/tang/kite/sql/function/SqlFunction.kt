package com.tang.kite.sql.function

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.expression.AggregateFunctionExpression
import com.tang.kite.sql.function.expression.FunctionExpression
import com.tang.kite.utils.Reflects.getColumnName
import kotlin.reflect.KProperty1

/**
 * Static factory methods for SQL functions.
 * Provides type-safe builders for common SQL functions.
 *
 * @author Tang
 */
object SqlFunction {

    /**
     * Returns the average value of a numeric column.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun avg(columnName: String): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.AVG, columnName)
    }

    /**
     * Returns the average value of a numeric column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> avg(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.AVG, column)
    }

    /**
     * Returns the average value of a numeric column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> avg(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.AVG, column)
    }

    /**
     * Returns any value in a column.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun anyValue(columnName: String): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.ANY_VALUE, columnName)
    }

    /**
     * Returns any value in a column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> anyValue(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.ANY_VALUE, column)
    }

    /**
     * Returns any value in a column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> anyValue(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.ANY_VALUE, column)
    }

    /**
     * Counts the number of rows or non-null values.
     *
     * @param columnName Column name or "*" for all rows
     */
    @JvmStatic
    @JvmOverloads
    fun count(columnName: String = SqlKeyword.STAR): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.COUNT, columnName)
    }

    /**
     * Counts the number of non-null values in a column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> count(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.COUNT, column)
    }

    /**
     * Counts the number of non-null values in a column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> count(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.COUNT, column)
    }

    /**
     * Returns the maximum value in a column.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun max(columnName: String): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MAX, columnName)
    }

    /**
     * Returns the maximum value in a column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> max(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MAX, column)
    }

    /**
     * Returns the maximum value in a column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> max(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MAX, column)
    }

    /**
     * Returns the minimum value in a column.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun min(columnName: String): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MIN, columnName)
    }

    /**
     * Returns the minimum value in a column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> min(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MIN, column)
    }

    /**
     * Returns the minimum value in a column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> min(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.MIN, column)
    }

    /**
     * Returns the sum of values in a column.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun sum(columnName: String): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.SUM, columnName)
    }

    /**
     * Returns the sum of values in a column.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> sum(column: KProperty1<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.SUM, column)
    }

    /**
     * Returns the sum of values in a column.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> sum(column: SFunction<T, *>): AggregateFunctionExpression {
        return AggregateFunctionExpression(FunctionName.SUM, column)
    }

    /**
     * Returns the length of a string.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun length(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.LENGTH, columnName)
    }

    /**
     * Returns the length of a string.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> length(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LENGTH, column)
    }

    /**
     * Returns the length of a string.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> length(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LENGTH, column)
    }

    /**
     * Converts a string to lowercase.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun lower(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.LOWER, columnName)
    }

    /**
     * Converts a string to lowercase.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> lower(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOWER, column)
    }

    /**
     * Converts a string to lowercase.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> lower(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOWER, column)
    }

    /**
     * Converts a string to uppercase.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun upper(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.UPPER, columnName)
    }

    /**
     * Converts a string to uppercase.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> upper(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.UPPER, column)
    }

    /**
     * Converts a string to uppercase.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> upper(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.UPPER, column)
    }

    /**
     * Removes leading and trailing whitespace from a string.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun trim(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.TRIM, columnName)
    }

    /**
     * Removes leading and trailing whitespace from a string.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> trim(column: KProperty1<T, *>): FunctionExpression {
        return trim(getColumnName(column))
    }

    /**
     * Removes leading and trailing whitespace from a string.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> trim(column: SFunction<T, *>): FunctionExpression {
        return trim(getColumnName(column))
    }

    /**
     * Removes specified characters from both ends of a string.
     *
     * @param char Character to remove
     * @param columnName Column name
     */
    @JvmStatic
    fun trimBoth(char: Any, columnName: String): FunctionExpression {
        val renders = SpaceArgs(
            KeywordArg(SqlKeyword.TRIM_BOTH),
            LiteralArg(char),
            KeywordArg(SqlKeyword.FROM),
            ColumnArg(Column(columnName))
        )
        return FunctionExpression(FunctionName.TRIM, renders)
    }

    /**
     * Removes specified characters from both ends of a string.
     *
     * @param char Character to remove
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> trimBoth(char: Any, column: KProperty1<T, *>): FunctionExpression {
        return trimBoth(char, getColumnName(column))
    }

    /**
     * Removes specified characters from both ends of a string.
     *
     * @param char Character to remove
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> trimBoth(char: Any, column: SFunction<T, *>): FunctionExpression {
        return trimBoth(char, getColumnName(column))
    }

    /**
     * Removes leading characters from a string.
     *
     * @param char Character to remove
     * @param columnName Column name
     */
    @JvmStatic
    fun trimLeading(char: Any, columnName: String): FunctionExpression {
        val renders = SpaceArgs(
            KeywordArg(SqlKeyword.TRIM_LEADING),
            LiteralArg(char),
            KeywordArg(SqlKeyword.FROM),
            ColumnArg(Column(columnName))
        )
        return FunctionExpression(FunctionName.TRIM, renders)
    }

    /**
     * Removes leading characters from a string.
     *
     * @param char Character to remove
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> trimLeading(char: Any, column: KProperty1<T, *>): FunctionExpression {
        return trimLeading(char, getColumnName(column))
    }

    /**
     * Removes leading characters from a string.
     *
     * @param char Character to remove
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> trimLeading(char: Any, column: SFunction<T, *>): FunctionExpression {
        return trimLeading(char, getColumnName(column))
    }

    /**
     * Removes trailing characters from a string.
     *
     * @param char Character to remove
     * @param columnName Column name
     */
    @JvmStatic
    fun trimTrailing(char: Any, columnName: String): FunctionExpression {
        val renders = SpaceArgs(
            KeywordArg(SqlKeyword.TRIM_TRAILING),
            LiteralArg(char),
            KeywordArg(SqlKeyword.FROM),
            ColumnArg(Column(columnName))
        )
        return FunctionExpression(FunctionName.TRIM, renders)
    }

    /**
     * Removes trailing characters from a string.
     *
     * @param char Character to remove
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> trimTrailing(char: Any, column: KProperty1<T, *>): FunctionExpression {
        return trimTrailing(char, getColumnName(column))
    }

    /**
     * Removes trailing characters from a string.
     *
     * @param char Character to remove
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> trimTrailing(char: Any, column: SFunction<T, *>): FunctionExpression {
        return trimTrailing(char, getColumnName(column))
    }

    /**
     * Concatenates multiple strings.
     *
     * @param renders Expressions to concatenate
     */
    private fun concat(vararg renders: FunctionRender): FunctionExpression {
        return FunctionExpression(FunctionName.CONCAT, CommaArgs(*renders))
    }

    /**
     * Concatenate multiple columns by column name string.
     *
     * @param columnNames Column names
     */
    @JvmStatic
    fun concat(vararg columnNames: String): FunctionExpression {
        val args = columnNames.map { ColumnArg(Column(it)) }.toTypedArray()
        return concat(*args)
    }

    /**
     * Concatenate multiple entity property columns.
     *
     * @param columns Kotlin property references to columns
     */
    @JvmStatic
    fun <T> concat(vararg columns: KProperty1<T, *>): FunctionExpression {
        val args = columns.map { ColumnArg(Column(it)) }.toTypedArray()
        return concat(*args)
    }

    /**
     * Concatenate multiple SFunction lambda columns.
     *
     * @param columns Lambda expressions to columns
     */
    @JvmStatic
    fun <T> concat(vararg columns: SFunction<T, *>): FunctionExpression {
        val args = columns.map { ColumnArg(Column(it)) }.toTypedArray()
        return concat(*args)
    }

    /**
     * Extracts a substring from a string.
     *
     * @param column Column object
     * @param start Starting position
     */
    private fun substring(column: Column, start: Int): FunctionExpression {
        return FunctionExpression(FunctionName.SUBSTRING, CommaArgs(ColumnArg(column), LiteralArg(start)))
    }

    /**
     * Extracts a substring from a string with fixed length.
     *
     * @param column Column object
     * @param start Starting position
     * @param length Length of substring
     */
    private fun substring(column: Column, start: Int, length: Int): FunctionExpression {
        return FunctionExpression(FunctionName.SUBSTRING, CommaArgs(ColumnArg(column), LiteralArg(start), LiteralArg(length)))
    }

    /**
     * Extracts a substring from a string.
     *
     * @param columnName Column name
     * @param start Starting position
     */
    @JvmStatic
    fun substring(columnName: String, start: Int): FunctionExpression {
        return substring(Column(columnName), start)
    }

    /**
     * Extracts a substring from a string with fixed length.
     *
     * @param columnName Column name
     * @param start Starting position
     * @param length Length of substring
     */
    @JvmStatic
    fun substring(columnName: String, start: Int, length: Int): FunctionExpression {
        return substring(Column(columnName), start, length)
    }

    /**
     * Extracts a substring from entity property.
     *
     * @param column Entity property column
     * @param start Starting position
     */
    @JvmStatic
    fun <T> substring(column: KProperty1<T, *>, start: Int): FunctionExpression {
        return substring(Column(column), start)
    }

    /**
     * Extracts a substring from entity property with fixed length.
     *
     * @param column Entity property column
     * @param start Starting position
     * @param length Length of substring
     */
    @JvmStatic
    fun <T> substring(column: KProperty1<T, *>, start: Int, length: Int): FunctionExpression {
        return substring(Column(column), start, length)
    }

    /**
     * Extracts a substring from lambda column.
     *
     * @param column SFunction lambda column getter
     * @param start Starting position
     */
    @JvmStatic
    fun <T> substring(column: SFunction<T, *>, start: Int): FunctionExpression {
        return substring(Column(column), start)
    }

    /**
     * Extracts a substring from lambda column with fixed length.
     *
     * @param column SFunction lambda column getter
     * @param start Starting position
     * @param length Length of substring
     */
    @JvmStatic
    fun <T> substring(column: SFunction<T, *>, start: Int, length: Int): FunctionExpression {
        return substring(Column(column), start, length)
    }

    /**
     * Replaces occurrences of a substring with another substring.
     *
     * @param column Column object
     * @param search The substring to search for and replace
     * @param replacement The new substring to use as replacement
     */
    private fun replace(column: Column, search: Any, replacement: Any): FunctionExpression {
        return FunctionExpression(FunctionName.REPLACE, CommaArgs(ColumnArg(column), LiteralArg(search), LiteralArg(replacement)))
    }

    /**
     * Replaces occurrences of a substring with another substring.
     *
     * @param columnName Raw database column name
     * @param search The substring to search for and replace
     * @param replacement The new substring to use as replacement
     */
    @JvmStatic
    fun replace(columnName: String, search: Any, replacement: Any): FunctionExpression {
        return replace(Column(columnName), search, replacement)
    }

    /**
     * Replaces occurrences of a substring with another substring.
     *
     * @param column Entity property column (supports val KProperty1 / var KMutableProperty1)
     * @param search The substring to search for and replace
     * @param replacement The new substring to use as replacement
     */
    @JvmStatic
    fun <T> replace(column: KProperty1<T, *>, search: Any, replacement: Any): FunctionExpression {
        return replace(Column(column), search, replacement)
    }

    /**
     * Replaces occurrences of a substring with another substring.
     *
     * @param column SFunction lambda column getter
     * @param search The substring to search for and replace
     * @param replacement The new substring to use as replacement
     */
    @JvmStatic
    fun <T> replace(column: SFunction<T, *>, search: Any, replacement: Any): FunctionExpression {
        return replace(Column(column), search, replacement)
    }

    /**
     * Returns the absolute value of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun abs(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.ABS, columnName)
    }

    /**
     * Returns the absolute value of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> abs(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ABS, column)
    }

    /**
     * Returns the absolute value of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> abs(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ABS, column)
    }

    /**
     * Rounds a number to specified decimal places.
     *
     * @param columnName Column name
     * @param scale Number of decimal places
     */
    @JvmStatic
    fun round(columnName: String, scale: Int): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(columnName)), LiteralArg(scale)))
    }

    /**
     * Rounds a number to integer.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun round(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(columnName))))
    }

    /**
     * Rounds a number to specified decimal places.
     *
     * @param column Kotlin property reference
     * @param scale Number of decimal places
     */
    @JvmStatic
    fun <T> round(column: KProperty1<T, *>, scale: Int): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(column)), LiteralArg(scale)))
    }

    /**
     * Rounds a number to integer.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> round(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(column))))
    }

    /**
     * Rounds a number to specified decimal places.
     *
     * @param column Lambda expression
     * @param scale Number of decimal places
     */
    @JvmStatic
    fun <T> round(column: SFunction<T, *>, scale: Int): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(column)), LiteralArg(scale)))
    }

    /**
     * Rounds a number to integer.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> round(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ROUND, CommaArgs(ColumnArg(Column(column))))
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun ceil(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.CEIL, columnName)
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> ceil(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.CEIL, column)
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> ceil(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.CEIL, column)
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun ceiling(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.CEILING, columnName)
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> ceiling(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.CEILING, column)
    }

    /**
     * Returns the smallest integer greater than or equal to a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> ceiling(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.CEILING, column)
    }

    /**
     * Returns the largest integer less than or equal to a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun floor(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.FLOOR, columnName)
    }

    /**
     * Returns the largest integer less than or equal to a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> floor(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.FLOOR, column)
    }

    /**
     * Returns the largest integer less than or equal to a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> floor(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.FLOOR, column)
    }

    /**
     * Returns the remainder of a division.
     *
     * @param columnName Raw database column name as dividend
     * @param divisor Divisor numeric value
     */
    @JvmStatic
    fun mod(columnName: String, divisor: Any): FunctionExpression {
        return FunctionExpression(FunctionName.MOD, CommaArgs(ColumnArg(Column(columnName)), LiteralArg(divisor)))
    }

    /**
     * Returns the remainder of a division.
     *
     * @param column Entity property column as dividend (supports val KProperty1 / var KMutableProperty1)
     * @param divisor Divisor numeric value
     */
    @JvmStatic
    fun <T> mod(column: KProperty1<T, *>, divisor: Any): FunctionExpression {
        return FunctionExpression(FunctionName.MOD, CommaArgs(ColumnArg(Column(column)), LiteralArg(divisor)))
    }

    /**
     * Returns the remainder of a division.
     *
     * @param column SFunction lambda column getter as dividend
     * @param divisor Divisor numeric value
     */
    @JvmStatic
    fun <T> mod(column: SFunction<T, *>, divisor: Any): FunctionExpression {
        return FunctionExpression(FunctionName.MOD, CommaArgs(ColumnArg(Column(column)), LiteralArg(divisor)))
    }

    /**
     * Returns the value of a number raised to the power of another number.
     *
     * @param columnName Raw database column name as base
     * @param exponent Exponent numeric value
     */
    @JvmStatic
    fun power(columnName: String, exponent: Any): FunctionExpression {
        return FunctionExpression(FunctionName.POWER, CommaArgs(ColumnArg(Column(columnName)), LiteralArg(exponent)))
    }

    /**
     * Returns the value of a number raised to the power of another number.
     *
     * @param column Entity property column as base (supports val KProperty1 / var KMutableProperty1)
     * @param exponent Exponent numeric value
     */
    @JvmStatic
    fun <T> power(column: KProperty1<T, *>, exponent: Any): FunctionExpression {
        return FunctionExpression(FunctionName.POWER, CommaArgs(ColumnArg(Column(column)), LiteralArg(exponent)))
    }

    /**
     * Returns the value of a number raised to the power of another number.
     *
     * @param column SFunction lambda column getter as base
     * @param exponent Exponent numeric value
     */
    @JvmStatic
    fun <T> power(column: SFunction<T, *>, exponent: Any): FunctionExpression {
        return FunctionExpression(FunctionName.POWER, CommaArgs(ColumnArg(Column(column)), LiteralArg(exponent)))
    }

    /**
     * Returns the square root of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun sqrt(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.SQRT, columnName)
    }

    /**
     * Returns the square root of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> sqrt(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SQRT, column)
    }

    /**
     * Returns the square root of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> sqrt(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SQRT, column)
    }

    /**
     * Returns the exponential value of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun exp(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.EXP, columnName)
    }

    /**
     * Returns the exponential value of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> exp(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.EXP, column)
    }

    /**
     * Returns the exponential value of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> exp(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.EXP, column)
    }

    /**
     * Returns the natural logarithm of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun log(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, columnName)
    }

    /**
     * Returns the natural logarithm of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> log(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, column)
    }

    /**
     * Returns the natural logarithm of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> log(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, column)
    }

    @JvmStatic
    fun log(base: Any, columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, LiteralArg(base), ColumnArg(Column(columnName)))
    }

    @JvmStatic
    fun <T> log(base: Any, column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, LiteralArg(base), ColumnArg(Column(column)))
    }

    @JvmStatic
    fun <T> log(base: Any, column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG, LiteralArg(base), ColumnArg(Column(column)))
    }

    /**
     * Returns the base-10 logarithm of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun log10(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.LOG10, columnName)
    }

    /**
     * Returns the base-10 logarithm of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> log10(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG10, column)
    }

    /**
     * Returns the base-10 logarithm of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> log10(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.LOG10, column)
    }

    /**
     * Returns the sine of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun sin(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.SIN, columnName)
    }

    /**
     * Returns the sine of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> sin(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SIN, column)
    }

    /**
     * Returns the sine of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> sin(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SIN, column)
    }

    /**
     * Returns the cosine of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun cos(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.COS, columnName)
    }

    /**
     * Returns the cosine of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> cos(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.COS, column)
    }

    /**
     * Returns the cosine of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> cos(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.COS, column)
    }

    /**
     * Returns the tangent of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun tan(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.TAN, columnName)
    }

    /**
     * Returns the tangent of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> tan(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.TAN, column)
    }

    /**
     * Returns the tangent of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> tan(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.TAN, column)
    }

    /**
     * Returns the arcsine of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun asin(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.ASIN, columnName)
    }

    /**
     * Returns the arcsine of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> asin(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ASIN, column)
    }

    /**
     * Returns the arcsine of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> asin(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ASIN, column)
    }

    /**
     * Returns the arccosine of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun acos(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.ACOS, columnName)
    }

    /**
     * Returns the arccosine of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> acos(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ACOS, column)
    }

    /**
     * Returns the arccosine of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> acos(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ACOS, column)
    }

    /**
     * Returns the arctangent of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun atan(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.ATAN, columnName)
    }

    /**
     * Returns the arctangent of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> atan(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ATAN, column)
    }

    /**
     * Returns the arctangent of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> atan(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.ATAN, column)
    }

    /**
     * Returns the sign of a number.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun sign(columnName: String): FunctionExpression {
        return FunctionExpression(FunctionName.SIGN, columnName)
    }

    /**
     * Returns the sign of a number.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> sign(column: KProperty1<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SIGN, column)
    }

    /**
     * Returns the sign of a number.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> sign(column: SFunction<T, *>): FunctionExpression {
        return FunctionExpression(FunctionName.SIGN, column)
    }

    /**
     * Returns the value of pi.
     */
    @JvmStatic
    fun pi(): FunctionExpression {
        return FunctionExpression(FunctionName.PI)
    }

    /**
     * Returns the current date.
     */
    @JvmStatic
    fun currentDate(): FunctionExpression {
        return FunctionExpression(FunctionName.CURRENT_DATE, withoutParentheses = true)
    }

    /**
     * Returns the current time.
     */
    @JvmStatic
    fun currentTime(): FunctionExpression {
        return FunctionExpression(FunctionName.CURRENT_TIME, withoutParentheses = true)
    }

    /**
     * Returns the current timestamp.
     */
    @JvmStatic
    fun currentTimestamp(): FunctionExpression {
        return FunctionExpression(FunctionName.CURRENT_TIMESTAMP, withoutParentheses = true)
    }

    private fun extract(unit: String, columnName: String): FunctionExpression {
        val renders = SpaceArgs(
            KeywordArg(unit),
            KeywordArg(SqlKeyword.FROM),
            ColumnArg(Column(columnName))
        )
        return FunctionExpression(FunctionName.EXTRACT, renders)
    }

    /**
     * Extracts the date part from a datetime expression.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun date(columnName: String): FunctionExpression {
        return extract(SqlKeyword.DATE, columnName)
    }

    /**
     * Extracts the date part from a datetime expression.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> date(column: KProperty1<T, *>): FunctionExpression {
        return date(getColumnName(column))
    }

    /**
     * Extracts the date part from a datetime expression.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> date(column: SFunction<T, *>): FunctionExpression {
        return date(getColumnName(column))
    }

    /**
     * Extracts the time part from a datetime expression.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun time(columnName: String): FunctionExpression {
        return extract(SqlKeyword.TIME, columnName)
    }

    /**
     * Extracts the time part from a datetime expression.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> time(column: KProperty1<T, *>): FunctionExpression {
        return time(getColumnName(column))
    }

    /**
     * Extracts the time part from a datetime expression.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> time(column: SFunction<T, *>): FunctionExpression {
        return time(getColumnName(column))
    }

    /**
     * Extracts the year from a date.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun year(columnName: String): FunctionExpression {
        return extract(SqlKeyword.YEAR, columnName)
    }

    /**
     * Extracts the year from a date.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> year(column: KProperty1<T, *>): FunctionExpression {
        return year(getColumnName(column))
    }

    /**
     * Extracts the year from a date.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> year(column: SFunction<T, *>): FunctionExpression {
        return year(getColumnName(column))
    }

    /**
     * Extracts the month from a date.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun month(columnName: String): FunctionExpression {
        return extract(SqlKeyword.MONTH, columnName)
    }

    /**
     * Extracts the month from a date.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> month(column: KProperty1<T, *>): FunctionExpression {
        return month(getColumnName(column))
    }

    /**
     * Extracts the month from a date.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> month(column: SFunction<T, *>): FunctionExpression {
        return month(getColumnName(column))
    }

    /**
     * Extracts the day from a date.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun day(columnName: String): FunctionExpression {
        return extract(SqlKeyword.DAY, columnName)
    }

    /**
     * Extracts the day from a date.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> day(column: KProperty1<T, *>): FunctionExpression {
        return day(getColumnName(column))
    }

    /**
     * Extracts the day from a date.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> day(column: SFunction<T, *>): FunctionExpression {
        return day(getColumnName(column))
    }

    /**
     * Extracts the hour from a time.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun hour(columnName: String): FunctionExpression {
        return extract(SqlKeyword.HOUR, columnName)
    }

    /**
     * Extracts the hour from a time.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> hour(column: KProperty1<T, *>): FunctionExpression {
        return hour(getColumnName(column))
    }

    /**
     * Extracts the hour from a time.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> hour(column: SFunction<T, *>): FunctionExpression {
        return hour(getColumnName(column))
    }

    /**
     * Extracts the minute from a time.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun minute(columnName: String): FunctionExpression {
        return extract(SqlKeyword.MINUTE, columnName)
    }

    /**
     * Extracts the minute from a time.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> minute(column: KProperty1<T, *>): FunctionExpression {
        return minute(getColumnName(column))
    }

    /**
     * Extracts the minute from a time.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> minute(column: SFunction<T, *>): FunctionExpression {
        return minute(getColumnName(column))
    }

    /**
     * Extracts the second from a time.
     *
     * @param columnName Column name
     */
    @JvmStatic
    fun second(columnName: String): FunctionExpression {
        return extract(SqlKeyword.SECOND, columnName)
    }

    /**
     * Extracts the second from a time.
     *
     * @param column Kotlin property reference
     */
    @JvmStatic
    fun <T> second(column: KProperty1<T, *>): FunctionExpression {
        return second(getColumnName(column))
    }

    /**
     * Extracts the second from a time.
     *
     * @param column Lambda expression
     */
    @JvmStatic
    fun <T> second(column: SFunction<T, *>): FunctionExpression {
        return second(getColumnName(column))
    }

    // TODO


    /**
     * Returns the first non-null value in a list.
     *
     * @param col Column object
     * @param default Default value if column is null
     */
    @JvmStatic
    fun coalesce(col: Column, default: Any): FunctionExpression {
        return FunctionExpression(FunctionName.COALESCE, ColumnArg(col), LiteralArg(default))
    }

    /**
     * Returns the first non-null value in a list of expressions.
     *
     * @param exprList Expressions to evaluate
     */
    @JvmStatic
    fun coalesce(vararg exprList: FunctionRender): FunctionExpression {
        return FunctionExpression(FunctionName.COALESCE, *exprList)
    }

    /**
     * Returns null if two expressions are equal, otherwise returns the first expression.
     *
     * @param expr1 First expression
     * @param expr2 Second expression
     */
    @JvmStatic
    fun nullif(expr1: FunctionRender, expr2: FunctionRender): FunctionExpression {
        return FunctionExpression(FunctionName.NULLIF, expr1, expr2)
    }

    /**
     * Returns the row number of the current row within its partition.
     */
    @JvmStatic
    fun rowNumber(): FunctionExpression {
        return FunctionExpression(FunctionName.ROW_NUMBER)
    }

    /**
     * Returns the rank of the current row within its partition.
     */
    @JvmStatic
    fun rank(): FunctionExpression {
        return FunctionExpression(FunctionName.RANK)
    }

    /**
     * Returns the dense rank of the current row within its partition.
     */
    @JvmStatic
    fun denseRank(): FunctionExpression {
        return FunctionExpression(FunctionName.DENSE_RANK)
    }

    /**
     * Divides the partition into N groups (tiles).
     *
     * @param numTiles Number of tiles
     */
    @JvmStatic
    fun ntile(numTiles: Int): FunctionExpression {
        return FunctionExpression(FunctionName.NTILE, LiteralArg(numTiles))
    }

    /**
     * Returns the value of a column from a previous row.
     *
     * @param col Column object
     * @param offset Number of rows back
     */
    @JvmStatic
    fun lag(col: Column, offset: Int = 1): FunctionExpression {
        return FunctionExpression(FunctionName.LAG, ColumnArg(col), LiteralArg(offset))
    }

    /**
     * Returns the value of a column from a following row.
     *
     * @param col Column object
     * @param offset Number of rows forward
     */
    @JvmStatic
    fun lead(col: Column, offset: Int = 1): FunctionExpression {
        return FunctionExpression(FunctionName.LEAD, ColumnArg(col), LiteralArg(offset))
    }

    /**
     * Returns the first value of an expression in a window frame.
     *
     * @param col Column object
     */
    @JvmStatic
    fun firstValue(col: Column): FunctionExpression {
        return FunctionExpression(FunctionName.FIRST_VALUE, ColumnArg(col))
    }

    /**
     * Returns the last value of an expression in a window frame.
     *
     * @param col Column object
     */
    @JvmStatic
    fun lastValue(col: Column): FunctionExpression {
        return FunctionExpression(FunctionName.LAST_VALUE, ColumnArg(col))
    }

    /**
     * Casts a value to a specified data type.
     *
     * @param col Column object
     * @param type Target data type
     */
    @JvmStatic
    fun cast(col: Column, type: String): FunctionExpression {
        return FunctionExpression(FunctionName.CAST, ColumnArg(col), LiteralArg(type))
    }

}
