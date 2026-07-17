package com.tang.kite.sql.function

/**
 * SQL standard & dialect function name constants
 *
 * @author Tang
 */
object FunctionName {

    // Aggregate Functions
    const val AVG = "avg"
    const val ANY_VALUE = "any_value"
    const val COUNT = "count"
    const val MAX = "max"
    const val MIN = "min"
    const val SUM = "sum"

    // String Functions
    const val LENGTH = "length"
    const val LOWER = "lower"
    const val UPPER = "upper"
    const val TRIM = "trim"
    const val CONCAT = "concat"
    const val SUBSTRING = "substring"
    const val REPLACE = "replace"
    const val POSITION = "position"

    // Numeric Math Functions
    const val ABS = "abs"
    const val ROUND = "round"
    const val CEIL = "ceil"
    const val CEILING = "ceiling"
    const val FLOOR = "floor"
    const val MOD = "mod"
    const val POWER = "power"
    const val SQRT = "sqrt"
    const val EXP = "exp"
    const val LOG = "log"
    const val LOG10 = "log10"
    const val LN = "ln"
    const val SIN = "sin"
    const val COS = "cos"
    const val TAN = "tan"
    const val ASIN = "asin"
    const val ACOS = "acos"
    const val ATAN = "atan"
    const val ATAN2 = "atan2"
    const val SIGN = "sign"
    const val PI = "pi"
    const val DEGREES = "degrees"
    const val RADIANS = "radians"

    // Date Time Functions
    const val CURRENT_DATE = "current_date"
    const val CURRENT_TIME = "current_time"
    const val CURRENT_TIMESTAMP = "current_timestamp"
    const val EXTRACT = "extract"

    // Conditional Functions
    const val COALESCE = "coalesce"
    const val NULLIF = "nullif"

    // Type Convert Standard Function
    const val CAST = "cast"

    // TODO
    // Window Functions
    const val ROW_NUMBER = "row_number"
    const val RANK = "rank"
    const val DENSE_RANK = "dense_rank"
    const val NTILE = "ntile"
    const val LAG = "lag"
    const val LEAD = "lead"
    const val FIRST_VALUE = "first_value"
    const val LAST_VALUE = "last_value"
    const val NTH_VALUE = "nth_value"
    const val PERCENT_RANK = "percent_rank"
    const val CUME_DIST = "cume_dist"

}
