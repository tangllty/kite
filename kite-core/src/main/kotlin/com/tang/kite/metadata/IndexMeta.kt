package com.tang.kite.metadata

import com.tang.kite.enumeration.IndexOrder

/**
 * Index metadata for database tables
 *
 * @author Tang
 */
class IndexMeta(

    /**
     * Database catalog
     */
    val catalog: String? = null,

    /**
     * Database schema
     */
    val schema: String? = null,

    /**
     * Table name
     */
    val tableName: String,

    /**
     * Index name
     */
    val indexName: String,

    /**
     * Columns in defined order
     */
    val columns: MutableList<String> = mutableListOf(),

    /**
     * Column sort order
     */
    val sorts: MutableList<IndexOrder> = mutableListOf(),

    /**
     * Whether the index is unique
     */
    val unique: Boolean,

    /**
     * Whether this index represents the primary key
     */
    val isPrimaryKey: Boolean

)
