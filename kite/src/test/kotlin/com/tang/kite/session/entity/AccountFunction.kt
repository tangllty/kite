package com.tang.kite.session.entity

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.math.BigDecimal
import java.sql.Date

/**
 * @author Tang
 */
@Table("account")
class AccountFunction (

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String = "",
    var password: String = "",
    var createTime: Date? = null,
    @Column("update_time")
    var updateTime: Date? = null,
    var balance: BigDecimal? = null,

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance)"
    }
}
