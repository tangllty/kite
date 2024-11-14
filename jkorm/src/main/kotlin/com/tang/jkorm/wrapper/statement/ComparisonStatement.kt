package com.tang.jkorm.wrapper.statement

import com.tang.jkorm.constants.SqlString.AND
import com.tang.jkorm.constants.SqlString.COMMA_SPACE
import com.tang.jkorm.constants.SqlString.LEFT_BRACKET
import com.tang.jkorm.constants.SqlString.QUESTION_MARK
import com.tang.jkorm.constants.SqlString.RIGHT_BRACKET
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator

/**
 * Comparison statement
 *
 * @author Tang
 */
class ComparisonStatement(val column: String, val value: Any, val comparisonOperator: ComparisonOperator) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        when (comparisonOperator) {
            ComparisonOperator.BETWEEN, ComparisonOperator.NOT_BETWEEN -> {
                val pair = value as Pair<*, *>
                sql.append("$column${comparisonOperator.value}$QUESTION_MARK$AND$QUESTION_MARK")
                parameters.addAll(pair.toList())
            }
            ComparisonOperator.IN, ComparisonOperator.NOT_IN -> {
                val list = value as Iterable<*>
                sql.append("$column${comparisonOperator.value}$LEFT_BRACKET")
                sql.append(list.joinToString(COMMA_SPACE) { QUESTION_MARK })
                sql.append(RIGHT_BRACKET)
                parameters.addAll(list)
            }
            ComparisonOperator.IS_NULL, ComparisonOperator.IS_NOT_NULL -> {
                sql.append("$column${comparisonOperator.value}")
            }
            else -> {
                sql.append("$column${comparisonOperator.value}$QUESTION_MARK")
                parameters.add(value)
            }
        }

    }

}
