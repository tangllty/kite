package com.tang.jkorm.wrapper.enumeration

import com.tang.jkorm.constants.SqlString

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
