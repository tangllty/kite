package com.tang.jkorm.wrapper.statement

import com.tang.jkorm.wrapper.enumeration.ComparisonOperator

/**
 * Comparison statement
 *
 * @author Tang
 */
class ComparisonStatement(val column: String, val value: Any, val comparisonOperator: ComparisonOperator) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        parameters.add(value)
        sql.append("$column${comparisonOperator.value}?")
    }

}
