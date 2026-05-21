package com.tang.kite.annotation

import com.tang.kite.enumeration.IndexOrder

/**
 * Database table index annotation
 * Supports: single column, composite, unique, full-text, spatial, prefix, sorted index
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Index(

    /**
     * Custom index name, auto-generated if empty
     */
    val name: String = "",

    /**
     * Whether the index in unique or not
     */
    val unique: Boolean = false,

    /**
     * Index sort order (ASC / DESC)
     */
    val order: IndexOrder = IndexOrder.ASC

)
