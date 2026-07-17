package com.tang.kite.sql.function

/**
 * SQL syntax keywords
 *
 * @author Tang
 */
object SqlKeyword {

    // Basic Symbols & Clause Keywords
    const val STAR = "*"
    const val FROM = "from"
    const val AS = "as"
    const val DISTINCT = "distinct"
    const val ALL = "all"
    const val IN = "in"

    // Trim Modifier Keywords
    const val TRIM_BOTH = "both"
    const val TRIM_LEADING = "leading"
    const val TRIM_TRAILING = "trailing"

    // Case Expression Keywords
    const val CASE = "case"
    const val WHEN = "when"
    const val THEN = "then"
    const val ELSE = "else"
    const val END = "end"

    // Datetime Unit Keywords (For EXTRACT function)
    const val DATE = "date"
    const val TIME = "time"
    const val YEAR = "year"
    const val MONTH = "month"
    const val DAY = "day"
    const val HOUR = "hour"
    const val MINUTE = "minute"
    const val SECOND = "second"

}
