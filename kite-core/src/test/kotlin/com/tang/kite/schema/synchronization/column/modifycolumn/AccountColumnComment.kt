package com.tang.kite.schema.synchronization.column.modifycolumn

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Test entity with updated column comments for schema synchronization test
 *
 * @author Tang
 */
@Table(value = "account", comment = "Table for account column comment")
class AccountColumnComment(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var password: String? = null,

    @Column(comment = "Register time")
    @CreateTime
    var createTime: LocalDateTime? = null,

    @UpdateTime
    var updateTime: LocalDateTime? = null,

    var balance: BigDecimal? = null

)
