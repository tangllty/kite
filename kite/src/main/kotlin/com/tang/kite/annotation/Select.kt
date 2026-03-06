package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Select(val value: String)
