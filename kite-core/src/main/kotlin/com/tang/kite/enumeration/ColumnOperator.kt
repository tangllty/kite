package com.tang.kite.enumeration

import com.tang.kite.sql.enumeration.ComparisonOperator

/**
 * Comparison operators
 *
 * @author Tang
 */
enum class ColumnOperator(val comparisonOperator: ComparisonOperator) {

    EQUAL(ComparisonOperator.EQUAL),

    NOT_EQUAL(ComparisonOperator.NOT_EQUAL),

    NOT_EQUALS_ALT(ComparisonOperator.NOT_EQUALS_ALT),

    EQ(ComparisonOperator.EQUAL),

    NE(ComparisonOperator.NOT_EQUAL),

    GT(ComparisonOperator.GT),

    GE(ComparisonOperator.GE),

    LT(ComparisonOperator.LT),

    LE(ComparisonOperator.LE),

    LIKE(ComparisonOperator.LIKE),

    LEFT_LIKE(ComparisonOperator.LIKE),

    RIGHT_LIKE(ComparisonOperator.LIKE),

    NOT_LIKE(ComparisonOperator.NOT_LIKE),

    NOT_LEFT_LIKE(ComparisonOperator.NOT_LIKE),

    NOT_RIGHT_LIKE(ComparisonOperator.NOT_LIKE),

    IS_NULL(ComparisonOperator.IS_NULL),

    IS_NOT_NULL(ComparisonOperator.IS_NOT_NULL)

}
