package com.tang.kite.schema.synchronization.column.initialcolumn

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
@Table(value = "column_account")
class AccountInitialColumn(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var password: String? = null

)
