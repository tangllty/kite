package com.tang.kite.schema.synchronization.column.dropcolumn

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import java.time.LocalDateTime

/**
 * Test entity with fewer columns for schema synchronization test (drop column scenario)
 *
 * @author Tang
 */
@Table(value = "account", comment = "Table for account drop column")
class AccountDropColumn(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    var username: String? = null,

    var password: String? = null,

    @CreateTime
    var createTime: LocalDateTime? = null,

    @UpdateTime
    var updateTime: LocalDateTime? = null

)
