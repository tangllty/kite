package com.tang.kite.annotation

/**
 * Annotation for defining custom SQL DELETE statements on Mapper methods.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Delete(val value: String)
