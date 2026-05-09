package com.tang.kite.annotation

/**
 * Composite index annotation for defining multi-column indexes on database tables
 *
 * @author Tang
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class CompositeIndex(

    /**
     * Custom index name, auto-generated if empty
     */
    val name: String = "",

    /**
     * Whether the index in unique or not
     */
    val unique: Boolean,

    /**
     * Index type
     */
    val type: String = "",

    /**
     * Column names included in the composite index
     */
    val columns: Array<String> = [],

    /**
     * Index sort orders for each column (ASC / DESC)
     */
    val orders: Array<String> = [],

    /**
     * Filter condition for partial index
     */
    val filterCondition: String = "",

    /**
     * Index comment
     */
    val comment: String = ""

)
