package com.tang.kite.annotation

/**
 * Annotation for defining custom SQL UPDATE statements on Mapper methods.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Update(val value: String)
