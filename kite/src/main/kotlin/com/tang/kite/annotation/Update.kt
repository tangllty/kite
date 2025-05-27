package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Update(val value: String)
