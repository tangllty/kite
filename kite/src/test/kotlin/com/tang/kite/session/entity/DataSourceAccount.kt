package com.tang.kite.session.entity

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
@Table("account")
class DataSourceAccount(

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    var username: String? = null,
    var password: String? = null

) {
    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password')"
    }
}
