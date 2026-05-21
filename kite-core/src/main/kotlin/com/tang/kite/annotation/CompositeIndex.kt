package com.tang.kite.annotation

import com.tang.kite.enumeration.IndexOrder

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
    val unique: Boolean = false,

    /**
     * Column names included in the composite index
     */
    val columns: Array<String> = [],

    /**
     * Index sort orders for each column (ASC / DESC)
     */
    val orders: Array<IndexOrder> = []

)
