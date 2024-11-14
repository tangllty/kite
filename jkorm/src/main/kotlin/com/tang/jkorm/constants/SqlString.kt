package com.tang.jkorm.constants

/**
 * SQL string constants
 *
 * @author Tang
 */
object SqlString {

    // Punctuation marks
    const val DOT = "."
    const val SPACE = " "
    const val COMMA = ","
    const val COMMA_SPACE = COMMA + SPACE
    const val EQUAL = " = "
    const val NOT_EQUAL = "!="
    const val NOT_EQUAL_ALT = "<>"
    const val GREATER_THAN = ">"
    const val GREATER_THAN_OR_EQUAL = ">="
    const val LESS_THAN = "<"
    const val LESS_THAN_OR_EQUAL = "<="
    const val PLUS = "+"
    const val MINUS = "-"
    const val PERCENT = "%"
    const val ASTERISK = "*"
    const val SEMICOLON = ";"
    const val UNDERSCORE = "_"
    const val SINGLE_QUOTE = "'"
    const val LEFT_BRACKET = "("
    const val RIGHT_BRACKET = ")"
    const val QUESTION_MARK = "?"

    // SQL keywords
    const val OR = " OR "
    const val IN = " IN "
    const val IS = " IS"
    const val NOT = " NOT"
    const val AND = " AND "
    const val SET = " SET "
    const val ASC = " ASC"
    const val DESC = " DESC"
    const val NULL = " NULL"
    const val LIKE = " LIKE "
    const val FROM = " FROM "
    const val WHERE = " WHERE "
    const val LIMIT = " LIMIT "
    const val SELECT = "SELECT "
    const val INSERT = "INSERT "
    const val UPDATE = "UPDATE "
    const val DELETE = "DELETE "
    const val VALUES = " VALUES "
    const val OFFSET = " OFFSET "
    const val BETWEEN = " BETWEEN "
    const val AS = " AS "
    const val UNION = " UNION "
    const val JOIN = " JOIN "
    const val HAVING = " HAVING "
    const val GROUP_BY = "GROUP BY "
    const val ORDER_BY = "ORDER BY "
    const val INTO = "INTO "
    const val DISTINCT = "DISTINCT "
    const val SELECT_DISTINCT = SELECT + DISTINCT
    const val AND_NOT = AND + "NOT "
    const val OR_NOT = OR + "NOT "
    const val NOT_LIKE = NOT + LIKE
    const val NOT_BETWEEN = NOT + BETWEEN
    const val NOT_IN = NOT + IN
    const val IS_NULL = IS + NULL
    const val IS_NOT_NULL = IS + NOT + NULL
    const val INSERT_INTO = INSERT + INTO
    const val DELETE_FROM = "DELETE$FROM"
    const val ROWS_FETCH_NEXT = " ROWS FETCH NEXT "
    const val ROWS_ONLY = " ROWS ONLY"

    // SQL statement
    const val SELECT_ALL_FROM = SELECT + ASTERISK + FROM
    const val SELECT_COUNT_FROM = SELECT + "COUNT(*)" + FROM

}
