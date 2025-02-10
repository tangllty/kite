package com.tang.kite.wrapper.statement

import com.tang.kite.constants.SqlString.AND
import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.LEFT_BRACKET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.RIGHT_BRACKET
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.enumeration.ComparisonOperator

/**
 * Comparison statement
 *
 * @author Tang
 */
class ComparisonStatement(val column: Column, val value: Any, val comparisonOperator: ComparisonOperator) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, withAlias: Boolean) {
        when (comparisonOperator) {
            ComparisonOperator.BETWEEN, ComparisonOperator.NOT_BETWEEN -> {
                val pair = value as Pair<*, *>
                sql.append("${column.toString(withAlias)}${comparisonOperator.value}$QUESTION_MARK$AND$QUESTION_MARK")
                parameters.addAll(pair.toList())
            }
            ComparisonOperator.IN, ComparisonOperator.NOT_IN -> {
                val list = value as Iterable<*>
                sql.append("${column.toString(withAlias)}${comparisonOperator.value}$LEFT_BRACKET")
                sql.append(list.joinToString(COMMA_SPACE) { QUESTION_MARK })
                sql.append(RIGHT_BRACKET)
                parameters.addAll(list)
            }
            ComparisonOperator.IS_NULL, ComparisonOperator.IS_NOT_NULL -> {
                sql.append("${column.toString(withAlias)}${comparisonOperator.value}")
            }
            else -> {
                sql.append("${column.toString(withAlias)}${comparisonOperator.value}$QUESTION_MARK")
                parameters.add(value)
            }
        }

    }

}
