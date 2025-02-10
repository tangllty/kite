package com.tang.kite.spring.entity

import com.tang.kite.annotation.id.Id

/**
 * @author Tang
 */
class Account (

    @Id
    var id: Long? = null,
    var username: String = "",
    var password: String = ""

) {
    override fun toString(): String {
        return "'Account => id=$id username=$username password=$password'"
    }
}
