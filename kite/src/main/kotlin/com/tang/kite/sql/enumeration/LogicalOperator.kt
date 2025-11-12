package com.tang.kite.sql.enumeration

import com.tang.kite.constants.SqlString

/**
 * Logical operators
 *
 * @author Tang
 */
enum class LogicalOperator(val value: String) {

    AND(SqlString.AND),

    OR(SqlString.OR),

    AND_NOT(SqlString.AND_NOT),

    OR_NOT(SqlString.OR_NOT)

}
