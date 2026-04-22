package com.tang.kite.session.entity

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
@Table("account")
class AccountAs(

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null,
    var createTime: LocalDateTime? = null,
    @Column("update_time")
    var updateTime: LocalDateTime? = null,
    var balance: BigDecimal? = null,
    @Column(ignore = true)
    var usernameAs: String? = null,
    @Column(ignore = true)
    var passwordAs: String? = null,

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance)"
    }
}
