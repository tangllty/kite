package com.tang.jkorm.annotation

/**
 * Table annotation
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Table(val value: String)
