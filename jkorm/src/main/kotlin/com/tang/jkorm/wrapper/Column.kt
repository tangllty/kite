package com.tang.jkorm.wrapper

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
class Column(

    val name: String,

    val tableAlias: String? = null

) {

    constructor(field: Field) : this(Reflects.getColumnName(field), Reflects.getTableAlias(field.declaringClass))

    constructor(column: KMutableProperty1<*, *>): this(column.javaField!!)

    constructor(column: SFunction<*, *>): this(Fields.getField(column))

    override fun toString(): String {
        return if (tableAlias == null) {
            name
        } else {
            "${tableAlias}.${name}"
        }
    }

    fun toString(withAlias: Boolean): String {
        return if (withAlias) {
            toString()
        } else {
            name
        }
    }

}
