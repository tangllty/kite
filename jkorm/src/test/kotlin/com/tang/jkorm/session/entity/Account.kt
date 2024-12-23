package com.tang.jkorm.session.entity

import com.tang.jkorm.annotation.Column
import com.tang.jkorm.annotation.id.Id
import com.tang.jkorm.annotation.id.IdType
import java.math.BigDecimal
import java.sql.Date

/**
 * @author Tang
 */
class Account (

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String = "",
    var password: String = "",
    var createTime: Date? = null,
    @Column("update_time")
    var updateTime: Date? = null,
    var balance: BigDecimal? = null

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance)"
    }
}
