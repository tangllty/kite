package com.tang.jkorm.session.entity

import com.tang.jkorm.annotation.Id

/**
 * @author Tang
 */
class Role(

    @Id(autoIncrement = false)
    var id: Long? = null,
    var name: String = ""

) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
