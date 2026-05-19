package com.tang.kite.sql

import com.tang.kite.constants.SqlString
import com.tang.kite.utils.Reflects
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class TableReference(

    val name: String,

    val clazz: Class<*>? = null,

    val alias: String? = null,

    val schema: String? = null,

    val lowercase: Boolean = true

) {

    constructor(clazz: Class<*>, lowercase: Boolean) : this(
        name = Reflects.getTableName(clazz).toCase(lowercase),
        clazz = clazz,
        alias = Reflects.getTableAlias(clazz).toCase(lowercase),
        lowercase = lowercase
    )

    constructor(clazz: KClass<*>, lowercase: Boolean) : this(clazz.java, lowercase)

    constructor(name: String, alias: String, lowercase: Boolean) : this(
        name = name.toCase(lowercase),
        alias = alias.toCase(lowercase),
        schema = null,
        lowercase = lowercase
    )

    constructor(name: String, lowercase: Boolean): this(name.toCase(lowercase), null, null, null, lowercase)

    constructor(clazz: Class<*>) : this(Reflects.getTableName(clazz), clazz, Reflects.getTableAlias(clazz))

    constructor(clazz: KClass<*>) : this(clazz.java)

    constructor(name: String, alias: String) : this(name, null, alias)

    fun toString(withAlias: Boolean): String {
        return if (withAlias) {
            name + SqlString.AS.toCase(lowercase) + alias
        } else {
            name
        }
    }

    override fun toString(): String {
        return toString(false)
    }

    companion object {

        private fun String.toCase(lowercase: Boolean): String {
            return if (lowercase) {
                this.lowercase()
            } else {
                this.uppercase()
            }
        }

    }

}
