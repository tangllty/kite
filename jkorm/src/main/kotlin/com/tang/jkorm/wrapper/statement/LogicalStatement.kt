package com.tang.jkorm.wrapper.statement

import com.tang.jkorm.constants.SqlString.LEFT_BRACKET
import com.tang.jkorm.constants.SqlString.RIGHT_BRACKET
import com.tang.jkorm.wrapper.enumeration.LogicalOperator

/**
 * Logical statement
 *
 * @author Tang
 */
class LogicalStatement(

    val condition: ComparisonStatement,

    var logicalOperator: LogicalOperator? = null,

    var nestedLogicalOperator: LogicalOperator? = null,

    var nestedConditions: MutableList<LogicalStatement> = mutableListOf()

) {

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, multiTableQuery: Boolean) {
        if (nestedConditions.isNotEmpty() && nestedConditions.last().nestedConditions.isEmpty()) {
            nestedConditions.last().logicalOperator = null
        }
        condition.appendSql(sql, parameters, multiTableQuery)
        logicalOperator?.let { sql.append(it.value) }
        if (nestedConditions.isEmpty()) {
            return
        }
        sql.append(LEFT_BRACKET)
        nestedConditions.forEach {
            it.appendSql(sql, parameters, multiTableQuery)
        }
        sql.append(RIGHT_BRACKET)
        nestedLogicalOperator?.let { sql.append(it.value) }
    }

}
