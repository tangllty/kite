package com.tang.jkorm.session.entity

import com.tang.jkorm.annotation.id.Id
import com.tang.jkorm.annotation.id.IdType

/**
 * @author Tang
 */
class Role(

    @Id(type = IdType.GENERATOR)
    var id: Long? = null,
    var name: String = ""

) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
