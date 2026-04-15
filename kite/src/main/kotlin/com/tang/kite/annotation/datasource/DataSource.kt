package com.tang.kite.annotation.datasource

/**
 * Data source annotation
 *
 * @author Tang
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DataSource(

    /**
     * Data source key
     */
    val value: String

)
