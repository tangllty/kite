package com.tang.kite.schema.synchronization.table.initialtable

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
@Table(value = "table_account", comment = "Table for account initial")
class AccountInitial(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var password: String? = null,

)
