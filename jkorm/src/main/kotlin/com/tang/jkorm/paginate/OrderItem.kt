package com.tang.jkorm.paginate

import com.tang.jkorm.function.SFunction
import com.tang.jkorm.wrapper.Column
import kotlin.reflect.KMutableProperty1

/**
 * Order item for paginate order by
 *
 * @author Tang
 */
class OrderItem<T>(

    var column: Column,

    var asc: Boolean = true

) {

    constructor(column: String, asc: Boolean = true) : this(Column(column), asc)

    constructor(property: KMutableProperty1<T, *>, asc: Boolean = true) : this(Column(property), asc)

    constructor(function: SFunction<T, *>, asc: Boolean = true) : this(Column(function), asc)

}
