package com.tang.kite.metadata

/**
 * Index metadata for database tables
 *
 * @author Tang
 */
class IndexMeta(

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
     * Index name
     */
    val indexName: String,

    /**
     * Whether the index is unique
     */
    val unique: Boolean,

    /**
     * Index structure (clustered, hashed, other)
     */
    val indexStructure: IndexStructure,

    /**
     * Index cardinality (number of unique values)
     */
    val cardinality: Long,

    /**
     * Number of pages used by the index
     */
    val pages: Long,

    /**
     * Filter condition for partial index
     */
    val filterCondition: String?,

    /**
     * Whether this index represents the primary key
     */
    val isPrimaryKey: Boolean

) {

    /**
     * Columns in defined order
     */
    val columns: MutableList<String> = mutableListOf()

    /**
     * Column sort order: A = ASC, D = DESC
     */
    val columnSortMap: MutableMap<String, String> = mutableMapOf()

    /**
     * Get columns in index definition order
     */
    fun getColumnsInOrder(): List<String> = columns

    /**
     * Check if this is a unique constraint (non-primary key)
     */
    fun isUniqueConstraint(): Boolean = unique && !isPrimaryKey

    /**
     * Check if this is a regular non-unique index
     */
    fun isRegularIndex(): Boolean = !unique

}
