package com.tang.kite.sql.function

import com.tang.kite.wrapper.Column

/**
 * @author Tang
 */
data class FunctionColumn (

    val function: String,

    var column: String

) : Column(column) {

    override fun toString(): String {
        return "$function($column)"
    }

}
