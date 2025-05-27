package com.tang.kite.annotation

/**
 * @author Tang
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Delete(val value: String)
