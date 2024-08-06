package com.tang.jkorm.annotation

/**
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Id(val autoIncrement: Boolean = true)
