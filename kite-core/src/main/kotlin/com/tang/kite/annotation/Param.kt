package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Param(val value: String)
