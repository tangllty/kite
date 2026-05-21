package com.tang.kite.schema.synchronization.index.addindex

import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Index
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.enumeration.IndexOrder
import java.time.LocalDateTime

/**
 * Test entity with fewer columns for schema synchronization test (add index scenario)
 *
 * @author Tang
 */
@CompositeIndex(unique = true, columns = ["username", "nickname"], orders = [IndexOrder.ASC, IndexOrder.DESC])
@Table(value = "account", comment = "Table for account add index")
class AccountAddIndex(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    @Index
    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null,

    @CreateTime
    var createTime: LocalDateTime? = null,

    @UpdateTime
    var updateTime: LocalDateTime? = null

)
