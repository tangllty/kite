package com.tang.kite.sql.function

/**
 * Keyword argument for SQL function parameters
 *
 * @author Tang
 */
class KeywordArg(private val keyword: String) : FunctionRender {

    override fun render(): String = keyword

}
