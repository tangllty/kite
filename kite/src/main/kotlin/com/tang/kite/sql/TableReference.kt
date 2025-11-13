package com.tang.kite.sql

import com.tang.kite.constants.SqlString
import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class TableReference(

    val name: String,

    val clazz: Class<*>,

    val alias: String,

    val schema: String? = null

) {

    constructor(clazz: Class<*>) : this(Reflects.getTableName(clazz), clazz, Reflects.getTableAlias(clazz))

    constructor(clazz: KClass<*>) : this(clazz.java)

    fun toString(withAlias: Boolean): String {
        return if (withAlias) {
            name + SqlString.AS + alias
        } else {
            name
        }
    }

}
