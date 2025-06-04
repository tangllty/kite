package com.tang.kite.wrapper.statement

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.AND
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.sql.function.FunctionColumn
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.enumeration.ComparisonOperator

/**
 * Comparison statement
 *
 * @author Tang
 */
class ComparisonStatement(val column: Column, val value: Any?, val comparisonOperator: ComparisonOperator) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, withAlias: Boolean) {
        when (comparisonOperator) {
            ComparisonOperator.BETWEEN, ComparisonOperator.NOT_BETWEEN -> {
                // TODO Pair's element type may be is FunctionColumn
                val pair = value as Pair<*, *>
                sql.append("${getColumn(withAlias)}${comparisonOperator.value}${getQuestionMark()}$AND${getQuestionMark()}")
                parameters.addAll(pair.toList())
            }
            ComparisonOperator.IN, ComparisonOperator.NOT_IN -> {
                // TODO List's element type may be is FunctionColumn
                val list = value as Iterable<*>
                sql.append("${getColumn(withAlias)}${comparisonOperator.value}$LEFT_BRACKET")
                sql.append(list.joinToString(COMMA_SPACE) { getQuestionMark() })
                sql.append(RIGHT_BRACKET)
                parameters.addAll(list)
            }
            ComparisonOperator.IS_NULL, ComparisonOperator.IS_NOT_NULL -> {
                sql.append("${getColumn(withAlias)}${comparisonOperator.value}")
            }
            else -> {
                sql.append("${getColumn(withAlias)}${comparisonOperator.value}${getQuestionMark()}")
                parameters.add(getColumnValue())
            }
        }

    }

    private fun getQuestionMark(): String {
        if (value is FunctionColumn) {
            val function = SqlConfig.getSql(value.function)
            return "$function($QUESTION_MARK)"
        }
        return QUESTION_MARK
    }

    private fun getColumn(withAlias: Boolean): String {
        if (column is FunctionColumn) {
            val function = SqlConfig.getSql(column.function)
            return "$function(${column.toString(withAlias)})"
        }
        return column.toString(withAlias)
    }

    private fun getColumnValue(): Any? {
        if (value is FunctionColumn) {
            return value.column
        }
        return value
    }

}
