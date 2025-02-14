package com.tang.kite.annotation

/**
 * Join annotation for join query
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Join(

    /**
     * Self field name
     */
    val selfField: String = "",

    /**
     * Target field name
     */
    val targetField: String = "",

    /**
     * Join table name
     */
    val joinTable: String = "",

    /**
     * Join self column name
     */
    val joinSelfColumn: String = "",

    /**
     * Join target column name
     */
    val joinTargetColumn: String = "",

)
