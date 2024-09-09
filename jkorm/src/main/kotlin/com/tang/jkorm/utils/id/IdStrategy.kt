package com.tang.jkorm.utils.id

import java.lang.reflect.Field

/**
 * @author Tang
 */
interface IdStrategy {

    fun getId(idField: Field): Any

}
