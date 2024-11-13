package com.tang.jkorm.wrapper

import com.tang.jkorm.sql.SqlStatement

/**
 * Marker interface for all wrappers
 *
 * @author Tang
 */
interface Wrapper<T> {

    fun getSqlStatement(): SqlStatement

}
