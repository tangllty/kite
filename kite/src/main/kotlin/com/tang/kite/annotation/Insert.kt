package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Insert(val value: String)
