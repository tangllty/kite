package com.tang.kite.sql.enumeration

import com.tang.kite.constants.SqlString

/**
 * @author Tang
 */
enum class JoinType(val sqlSymbol: String) {

    LEFT(SqlString.LEFT_JOIN),

    RIGHT(SqlString.RIGHT_JOIN),

    INNER(SqlString.INNER_JOIN),

    FULL(SqlString.FULL_JOIN),

    CROSS(SqlString.CROSS_JOIN)

}
