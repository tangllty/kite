package com.tang.kite.paginate

import java.io.Serializable

/**
 * Page
 *
 * @param T Entity type
 *
 * @author Tang
 */
class Page<T> : Serializable {

    constructor()

    constructor(rows: List<T>, total: Long, pageNumber: Long, pageSize: Long) {
        this.rows = rows
        this.total = total
        this.pageNumber = pageNumber
        this.pageSize = pageSize
    }

    /**
     * Rows data
     */
    var rows: List<T> = emptyList()

    /**
     * Total count
     */
    var total: Long = 0

    /**
     * Page number
     */
    var pageNumber: Long = 1

    /**
     * Page size
     */
    var pageSize: Long = 10

    /**
     * Status code
     */
    var code: Int = 200

    /**
     * Message
     */
    var msg: String = "Success"

}
