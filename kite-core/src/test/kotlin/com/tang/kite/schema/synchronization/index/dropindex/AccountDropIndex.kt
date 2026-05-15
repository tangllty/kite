package com.tang.kite.schema.synchronization.index.dropindex

import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.time.LocalDateTime

/**
 * Test entity with fewer columns for schema synchronization test (drop index scenario)
 *
 * @author Tang
 */
@CompositeIndex(unique = true, columns = ["username", "nickname"], orders = ["asc", "desc"])
@Table(value = "account", comment = "Table for account drop index")
class AccountDropIndex(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null,

    @CreateTime
    var createTime: LocalDateTime? = null,

    @UpdateTime
    var updateTime: LocalDateTime? = null

)
