package com.tang.jkorm.wrapper.statement

import com.tang.jkorm.wrapper.enumeration.LogicalOperator

/**
 * Logical statement
 *
 * @author Tang
 */
class LogicalStatement(val condition: ComparisonStatement, var logicalOperator: LogicalOperator? = null) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        condition.appendSql(sql, parameters)
        logicalOperator?.let { sql.append(it.value) }
    }

}
