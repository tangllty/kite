package com.tang.kite.annotation.id.strategy

import java.lang.reflect.Field

/**
 * @author Tang
 */
interface IdStrategy {

    fun getId(idField: Field): Any

}
