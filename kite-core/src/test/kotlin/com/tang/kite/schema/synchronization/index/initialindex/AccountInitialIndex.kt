package com.tang.kite.schema.synchronization.index.initialindex

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
@Table(value = "index_account")
class AccountInitialIndex(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null

)
