package com.tang.kite.annotation

/**
 * Annotation for specifying parameter names in custom SQL methods.
 * Used to bind method parameters to named parameters in SQL statements.
 *
 * @author Tang
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Param(val value: String)
