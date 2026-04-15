package com.tang.kite.spring.entity

import com.tang.kite.annotation.id.Id
import java.math.BigDecimal
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
    var updateTime: LocalDateTime? = null,
    var balance: BigDecimal? = null,

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance)"
    }
}
