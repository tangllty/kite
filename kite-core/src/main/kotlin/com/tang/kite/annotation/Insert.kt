package com.tang.kite.annotation

/**
 * Annotation for defining custom SQL INSERT statements on Mapper methods.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Insert(val value: String)
