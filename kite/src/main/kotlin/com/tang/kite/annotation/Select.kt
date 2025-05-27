package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Select(val value: String)
