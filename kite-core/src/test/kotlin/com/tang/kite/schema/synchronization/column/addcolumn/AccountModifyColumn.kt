package com.tang.kite.schema.synchronization.column.addcolumn

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Test entity with additional column for schema synchronization test
 *
 * @author Tang
 */
@Table(value = "account", comment = "Table for account modify column comment")
class AccountModifyColumn(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var password: String? = null,

    @Column(comment = "Create time")
    @CreateTime
    var createTime: LocalDateTime? = null,

    @Column(comment = "Update time")
    @UpdateTime
    var updateTime: LocalDateTime? = null,

    var balance: BigDecimal? = null

)
