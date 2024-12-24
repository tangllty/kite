package com.tang.jkorm.annotation

/**
 * Column annotation
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Column(

    /**
     * Column name
     */
    val value: String = "",

    /**
     * Ignore this column when generate SQL
     */
    val ignore: Boolean = false

)
