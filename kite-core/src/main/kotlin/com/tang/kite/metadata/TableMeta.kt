package com.tang.kite.metadata

/**
 * Table metadata
 *
 * @author Tang
 */
class TableMeta(

    /**
     * Database catalog
     */
    val catalog: String?,

    /**
     * Database schema
     */
    val schema: String?,

    /**
     * Table name
     */
    val tableName: String,

    /**
     * Table type (TABLE, VIEW, etc.)
     */
    val tableType: String,

    /**
     * Table comment/description
     */
    val comment: String?

)
