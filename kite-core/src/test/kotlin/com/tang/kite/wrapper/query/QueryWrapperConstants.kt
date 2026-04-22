package com.tang.kite.wrapper.query

/**
 * @author Tang
 */
open class QueryWrapperConstants {

    val allColumns = "id, username, password, create_time, update_time, balance"

    val selectFromAccount = "select $allColumns from account"

}
