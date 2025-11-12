package com.tang.kite.wrapper

import com.tang.kite.sql.statement.SqlStatement

/**
 * Marker interface for all wrappers
 *
 * @author Tang
 */
interface Wrapper<T> {

    fun getSqlStatement(): SqlStatement

    fun checkValues()

}
