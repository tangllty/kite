package com.tang.kite.annotation.field

/**
 * Annotation for automatic update-time field filling.
 * When applied to a field, its value will be automatically set to the current time
 * during both INSERT and UPDATE operations.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class UpdateTime
