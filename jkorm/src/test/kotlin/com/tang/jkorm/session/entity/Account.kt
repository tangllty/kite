package com.tang.jkorm.session.entity

import com.tang.jkorm.annotation.Id

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
