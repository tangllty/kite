package com.tang.kite.schema.builder

import com.tang.kite.annotation.CompositeIndex
import com.tang.kite.annotation.Index
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.enumeration.SortOrder
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
@CompositeIndex(unique = true, columns = ["username", "nickname"], orders = [SortOrder.ASC, SortOrder.DESC])
@Table(value = "account", comment = "Table for account builder")
class AccountBuilder(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    @Index(order = SortOrder.DESC)
    var username: String? = null,

    var nickname: String? = null,

    var password: String? = null,

    @CreateTime
    var createTime: LocalDateTime? = null,

    @UpdateTime
    var updateTime: LocalDateTime? = null,

    var balance: BigDecimal? = null

)
