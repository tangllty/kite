package com.tang.kite.annotation

/**
 * Annotation for defining custom SQL SELECT statements on Mapper methods.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Select(val value: String)
