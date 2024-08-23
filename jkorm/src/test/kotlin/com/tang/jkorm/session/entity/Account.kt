package com.tang.jkorm.session.entity

import com.tang.jkorm.annotation.Id
import java.time.LocalDateTime

/**
 * @author Tang
 */
class Account (

    @Id
    var id: Long? = null,
    var username: String = "",
    var password: String = "",
    var createTime: LocalDateTime? = null,
    var balance: Double? = null

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, balance=$balance)"
    }
}
