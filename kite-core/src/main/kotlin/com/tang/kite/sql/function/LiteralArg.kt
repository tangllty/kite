package com.tang.kite.sql.function

/**
 * Literal constant argument for SQL function parameters
 *
 * @author Tang
 */
class LiteralArg(private val value: Any?) : FunctionRender {

    override fun render(): String = when (value) {
        is String -> "'$value'"
        null -> "NULL"
        else -> value.toString()
    }

}
