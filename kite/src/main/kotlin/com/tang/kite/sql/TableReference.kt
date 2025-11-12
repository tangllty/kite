package com.tang.kite.sql

import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class TableReference(

    val name: String,

    var clazz: Class<*>? = null,

    val alias: String? = null,

    val schema: String? = null

) {

    constructor(clazz: Class<*>) : this(name = Reflects.getTableName(clazz), clazz = clazz)

    constructor(clazz: KClass<*>) : this(clazz.java)

}
