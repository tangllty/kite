package com.tang.kite.sql.function

import com.tang.kite.sql.Column

/**
 * Column argument for SQL function parameters
 *
 * @author Tang
 */
open class ColumnArg(val column: Column) : FunctionRender {

    override fun render(): String = column.toString()

}
