package com.tang.kite.annotation

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
    val unique: Boolean,

    /**
     * Index type
     */
    val type: String = "",

    /**
     * Index sort order (ASC / DESC)
     */
    val order: String = "",

    /**
     * Filter condition for partial index
     */
    val filterCondition: String = "",

    /**
     * Index comment
     */
    val comment: String = ""

)
