package com.tang.kite.sql.function

import com.tang.kite.sql.Column

/**
 * Column argument for SQL function parameters
 *
 * @author Tang
 */
class ModifierColumnArg(column: Column) : ColumnArg(column) {

    private var distinct = false

    private var all = false

    /**
     * Add DISTINCT modifier
     *
     * @return this
     */
    fun distinct(): FunctionRender {
        distinct = true
        all = false
        return this
    }

    /**
     * Add ALL modifier
     *
     * @return this
     */
    fun all(): FunctionRender {
        all = true
        distinct = false
        return this
    }

    override fun render(): String {
        return if (distinct) {
            SqlKeyword.DISTINCT + " " + super.render()
        } else if (all) {
            SqlKeyword.ALL + " " + super.render()
        } else {
            super.render()
        }
    }

}
