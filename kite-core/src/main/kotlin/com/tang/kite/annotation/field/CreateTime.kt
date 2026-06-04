package com.tang.kite.annotation.field

/**
 * Annotation for automatic create-time field filling.
 * When applied to a field, its value will be automatically set to the current time
 * during INSERT operations.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class CreateTime
