package com.tang.kite.session.entity

import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
@Table(value = "role", alias = "re")
class Role(

    @Id(type = IdType.GENERATOR)
    var id: Long? = null,
    var name: String = ""

) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
