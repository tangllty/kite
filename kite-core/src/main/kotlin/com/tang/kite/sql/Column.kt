package com.tang.kite.sql

import com.tang.kite.constants.SqlString
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
open class Column(

    val name: String?,

    val field: Field? = null,

    var tableAlias: String? = null

) {

    constructor(field: Field) : this(Reflects.getColumnName(field), field)

    constructor(column: KMutableProperty1<*, *>): this(column.javaField!!)

    constructor(column: SFunction<*, *>): this(Reflects.getField(column))

    constructor(name: String, tableAlias: String) : this(name, null, tableAlias)

    override fun toString(): String {
        if (name == null) {
            throw IllegalArgumentException("Column name cannot be null")
        }
        return if (tableAlias == null) {
            name
        } else {
            "${tableAlias}${SqlString.DOT}${name}"
        }
    }

    fun toString(withAlias: Boolean): String {
        if (name == null) {
            throw IllegalArgumentException("Column name cannot be null")
        }
        return if (withAlias) {
            if (field == null) {
                throw IllegalArgumentException("Column field cannot be null")
            }
            tableAlias = Reflects.getTableAlias(field.declaringClass)
            toString()
        } else {
            name
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Column) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }

}
