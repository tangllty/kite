package com.tang.kite.sql

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

    val tableAlias: String? = null

) {

    constructor(field: Field) : this(Reflects.getColumnName(field), Reflects.getTableAlias(field.declaringClass))

    constructor(column: KMutableProperty1<*, *>): this(column.javaField!!)

    constructor(column: SFunction<*, *>): this(Reflects.getField(column))

    override fun toString(): String {
        if (name == null) {
            throw IllegalArgumentException("Column name cannot be null")
        }
        return if (tableAlias == null) {
            name
        } else {
            "${tableAlias}.${name}"
        }
    }

    fun toString(withAlias: Boolean): String {
        if (name == null) {
            throw IllegalArgumentException("Column name cannot be null")
        }
        return if (withAlias) {
            toString()
        } else {
            name
        }
    }

}
