package com.tang.kite.sql.function.expression

import com.tang.kite.config.SqlConfig
import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.ColumnArg
import com.tang.kite.sql.function.FunctionRender
import com.tang.kite.utils.Reflects
import kotlin.reflect.KProperty1

/**
 * General SQL function expression
 *
 * @author Tang
 */
open class FunctionExpression(

    val function: String,

    val renderList: MutableList<FunctionRender> = mutableListOf(),

    private val withoutParentheses: Boolean = false

) : Column(""), FunctionRender {

    constructor(function: String, columnName: String) : this(function, mutableListOf(ColumnArg(Column(columnName))))

    constructor(function: String, column: KProperty1<*, *>) : this(function, Reflects.getColumnName(column))

    constructor(function: String, column: SFunction<*, *>) : this(function, Reflects.getColumnName(column))

    constructor(function: String, vararg renders: FunctionRender) : this(function, mutableListOf(*renders))

    override fun render(): String {
        val functionName = SqlConfig.getSql(function)
        if (withoutParentheses) {
            return functionName
        }
        val argText = renderList.joinToString("") { it.render() }
        return "$functionName($argText)"
    }

    override fun toString() = render()

}
