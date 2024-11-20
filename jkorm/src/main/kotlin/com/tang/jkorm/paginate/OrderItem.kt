package com.tang.jkorm.paginate

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Reflects.getColumnName
import kotlin.reflect.KMutableProperty1

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
        this.column = getColumnName(property)
        this.asc = asc
    }

    constructor(function: SFunction<T, *>, asc: Boolean = true) {
        this.column = getColumnName(function)
        this.asc = asc
    }

}
