package com.tang.kite.schema.synchronization.index.modifyindex

import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Index
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.enumeration.IndexOrder

/**
 * @author Tang
 */
@CompositeIndex(unique = true, columns = ["username", "nickname"], orders = [IndexOrder.ASC, IndexOrder.DESC])
@Table(value = "index_account")
class AccountModifyIndex(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    @Index(order = IndexOrder.DESC)
    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null

)
