package com.tang.jkorm.wrapper.enumeration

import com.tang.jkorm.constants.SqlString

/**
 * Comparison operators
 *
 * @author Tang
 */
enum class ComparisonOperator(val value: String) {

    EQUAL(SqlString.EQUAL),

    NOT_EQUAL(SqlString.NOT_EQUAL),

    NOT_EQUALS_ALT(SqlString.NOT_EQUAL_ALT),

    GT(SqlString.GREATER_THAN),

    GE(SqlString.GREATER_THAN_OR_EQUAL),

    LT(SqlString.LESS_THAN),

    LE(SqlString.LESS_THAN_OR_EQUAL),

    LIKE(SqlString.LIKE),

    BETWEEN(SqlString.BETWEEN),

    IN(SqlString.IN),

    NOT_LIKE(SqlString.NOT_LIKE),

    NOT_BETWEEN(SqlString.NOT_BETWEEN),

    NOT_IN(SqlString.NOT_IN),

    IS_NULL(SqlString.IS_NULL),

    IS_NOT_NULL(SqlString.IS_NOT_NULL)

}
