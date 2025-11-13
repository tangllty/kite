package com.tang.kite.constants

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
    const val NOT_EQUAL = " != "
    const val NOT_EQUAL_ALT = " <> "
    const val GREATER_THAN = " > "
    const val GREATER_THAN_OR_EQUAL = " >= "
    const val LESS_THAN = " < "
    const val LESS_THAN_OR_EQUAL = " <= "
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
    const val OR = " or "
    const val IN = " in "
    const val IS = " is"
    const val NOT = " not"
    const val AND = " and "
    const val SET = " set "
    const val ASC = " asc"
    const val DESC = " desc"
    const val NULL = " null"
    const val LIKE = " like "
    const val FROM = " from "
    const val WHERE = " where "
    const val LIMIT = " limit "
    const val SELECT = "select "
    const val INSERT = "insert "
    const val UPDATE = "update "
    const val DELETE = "delete "
    const val VALUES = " values "
    const val OFFSET = " offset "
    const val BETWEEN = " between "
    const val AS = " as "
    const val ON = " on "
    const val UNION = " union "
    const val JOIN = " join "
    const val LEFT_JOIN = " left$JOIN"
    const val RIGHT_JOIN = " right$JOIN"
    const val INNER_JOIN = " inner$JOIN"
    const val FULL_JOIN = " full$JOIN"
    const val CROSS_JOIN = " cross$JOIN"
    const val HAVING = " having "
    const val GROUP_BY = " group by "
    const val ORDER_BY = " order by "
    const val INTO = "into "
    const val DISTINCT = "distinct "
    const val SELECT_DISTINCT = SELECT + DISTINCT
    const val AND_NOT = AND + "not "
    const val OR_NOT = OR + "not "
    const val NOT_LIKE = NOT + LIKE
    const val NOT_BETWEEN = NOT + BETWEEN
    const val NOT_IN = NOT + IN
    const val IS_NULL = IS + NULL
    const val IS_NOT_NULL = IS + NOT + NULL
    const val INSERT_INTO = INSERT + INTO
    const val DELETE_FROM = "delete$FROM"
    const val ROWS_FETCH_NEXT = " rows fetch next "
    const val ROWS_ONLY = " rows only"

    // SQL statement
    const val SELECT_ALL_FROM = SELECT + ASTERISK + FROM
    const val SELECT_COUNT_FROM = SELECT + "count(*)" + FROM

}
