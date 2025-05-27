package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Param(val value: String)
