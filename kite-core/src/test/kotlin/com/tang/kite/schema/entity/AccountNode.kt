package com.tang.kite.schema.entity

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.fill.CreateTime
import com.tang.kite.annotation.fill.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.enumeration.ColumnOperator
import com.tang.kite.sql.datatype.DataType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
@Table(comment = "Table for account nodes")
class AccountNode(

    @Id(type = IdType.AUTO)
    var id: Long? = null,

    @Column(operator = ColumnOperator.LIKE, length = 20, index = true)
    var username: String? = null,

    var password: String? = null,

    @Column(value ="create_time", dataType = DataType.TIMESTAMP)
    @CreateTime
    var createTime: LocalDateTime? = null,

    @Column(value = "update_time", dataType = DataType.TIMESTAMP)
    @UpdateTime
    var updateTime: LocalDateTime? = null,

    @Column(dataType = DataType.DECIMAL, precision = 10, scale = 2, comment = "Balance")
    var balance: BigDecimal? = null,

    @Column(ignore = true)
    var beIgnore: String? = null

) {

    override fun toString(): String {
        return "Account(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance)"
    }

}
