package com.tang.kite.sql.function.expression

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.function.FunctionRender
import com.tang.kite.sql.function.ModifierColumnArg
import com.tang.kite.utils.Reflects
import kotlin.reflect.KProperty1

/**
 * @author Tang
 */
class AggregateFunctionExpression(

    function: String,

    renderList: MutableList<FunctionRender> = mutableListOf()

) : FunctionExpression(function, renderList) {

    constructor(function: String, columnName: String) : this(function, mutableListOf(ModifierColumnArg(Column(columnName))))

    constructor(function: String, column: KProperty1<*, *>) : this(function, Reflects.getColumnName(column))

    constructor(function: String, column: SFunction<*, *>) : this(function, Reflects.getColumnName(column))

    fun distinct(): FunctionExpression {
        val column = renderList.firstOrNull() as ModifierColumnArg
        column.distinct()
        return this
    }

    fun all(): FunctionExpression {
        val column = renderList.firstOrNull() as ModifierColumnArg
        column.all()
        return this
    }

}
