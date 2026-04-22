package com.tang.kite.session.entity

import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType

/**
 * @author Tang
 */
class Role(

    @Id(type = IdType.GENERATOR)
    var id: Long? = null,
    var name: String? = null

) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
