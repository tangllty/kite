package com.tang.kite.optimistic.entity

import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.field.CreateTime
import com.tang.kite.annotation.field.UpdateTime
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.enumeration.ColumnOperator
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
@Table("account")
class OptimisticLockAccount(

    @Id(type = IdType.AUTO)
    var id: Long? = null,
    @Column(operator = ColumnOperator.LIKE)
    var username: String? = null,
    var password: String? = null,
    @CreateTime
    var createTime: LocalDateTime? = null,
    @UpdateTime
    var updateTime: LocalDateTime? = null,
    var balance: BigDecimal? = null,
    @Version
    var version: Long? = null

) {
    override fun toString(): String {
        return "OptimisticLockAccount(id=$id, username='$username', password='$password', createTime=$createTime, updateTime=$updateTime, balance=$balance, version=$version)"
    }
}
