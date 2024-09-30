package com.tang.jkorm.paginate

/**
 * Order item for paginate order by
 *
 * @author Tang
 */
class OrderItem {

    var column: String = ""

    var asc: Boolean = true

    constructor(column: String, asc: Boolean) {
        this.column = column
        this.asc = asc
    }

}
