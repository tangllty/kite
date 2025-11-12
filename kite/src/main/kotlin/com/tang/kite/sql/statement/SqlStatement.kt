package com.tang.kite.sql.statement

import com.tang.kite.constants.SqlString
import com.tang.kite.utils.Statements
import java.sql.Date
import java.sql.PreparedStatement
import kotlin.collections.iterator

/**
 * SQL statement
 *
 * @author Tang
 */
class SqlStatement(

    val sql: String,

    val parameters: MutableList<Any?>

) {

    fun getActualSql(): String {
        val placeholderCount = sql.count { it == SqlString.QUESTION_MARK.first() }
        if (placeholderCount == 0) {
            return sql // No placeholders, return the SQL as is
        }
        if (placeholderCount != parameters.size) {
            throw IllegalArgumentException("The number of placeholders in the SQL statement does not match the number of parameters provided for a single execution.")
        }
        val sqlParts = sql.split(SqlString.QUESTION_MARK)
        val resolvedSql = StringBuilder()
        for (i in sqlParts.indices) {
            resolvedSql.append(sqlParts[i])
            if (i < parameters.size) {
                resolvedSql.append(getParameter(parameters[i]))
            }
        }
        return resolvedSql.toString()
    }

    private fun getParameter(parameter: Any?): String {
        return when (parameter) {
            null -> "NULL"
            is String -> "'${escapeSql(parameter)}'"
            is Number, is Boolean -> parameter.toString()
            is Date -> "'$parameter'"
            is java.util.Date -> "'${Date(parameter.time)}'"
            else -> "'${escapeSql(parameter.toString())}'"
        }
    }

    val escapedMap = mapOf(
        "'" to "''",
        "\\" to "\\\\",
        "\n" to "\\n",
        "\r" to "\\r",
        "\t" to "\\t"
    )

    private fun escapeSql(value: String): String {
        var escapedValue = value
        for ((key, replacement) in escapedMap) {
            escapedValue = escapedValue.replace(key, replacement)
        }
        return escapedValue
    }

    fun setValues(statement: PreparedStatement) {
        Statements.setValues(statement, parameters)
    }

}
