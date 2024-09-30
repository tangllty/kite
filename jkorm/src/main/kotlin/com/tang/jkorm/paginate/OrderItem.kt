package com.tang.jkorm.paginate

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Order item for paginate order by
 *
 * @author Tang
 */
class OrderItem<T> {

    var column: String = ""

    var asc: Boolean = true

    constructor(column: String, asc: Boolean = true) {
        this.column = column
        this.asc = asc
    }

    constructor(property: KMutableProperty1<T, *>, asc: Boolean = true) {
        var filed = property.javaField
        this.column = Reflects.getColumnName(filed!!)
        this.asc = asc
    }

    constructor(function: SFunction<T, *>, asc: Boolean = true) {
        val filed = Fields.getField(function)
        this.column = Reflects.getColumnName(filed)
        this.asc = asc
    }

}
