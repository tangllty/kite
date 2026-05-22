package com.tang.kite.schema.synchronization.index.dropindex

import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.enumeration.IndexOrder

/**
 * @author Tang
 */
@CompositeIndex(unique = true, columns = ["username", "nickname"], orders = [IndexOrder.ASC, IndexOrder.DESC])
@Table(value = "index_account")
class AccountDropIndex(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null

)
