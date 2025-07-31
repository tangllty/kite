package com.tang.kite.session.entity

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Join
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.math.BigDecimal
import java.sql.Date

/**
 * @author Tang
 */
@Table("account")
class AccountOneToOneWIthJoinTable (

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null,
    var createTime: Date? = null,
    @Column("update_time")
    var updateTime: Date? = null,
    var balance: BigDecimal? = null,
    @Column(ignore = true)
    var beIgnore: String? = null,
    @Join(
        selfField = "id",
        targetField = "id",
        joinTable = "account_role",
        joinSelfColumn = "account_id",
        joinTargetColumn = "role_id"
    )
    var role: Role? = null,

) {
    override fun toString(): String {
        return "AccountOneToOneWIthJoinTable(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance, beIgnore='$beIgnore', role=$role)"
    }
}
