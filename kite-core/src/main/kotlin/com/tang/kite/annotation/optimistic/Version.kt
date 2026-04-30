package com.tang.kite.annotation.optimistic

/**
 * Version annotation for optimistic locking
 *
 * Marks a field as the version field for optimistic locking.
 * When updating an entity, the version field will be automatically incremented
 * and used in the WHERE clause to ensure data consistency.
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Version(
    /**
     * Initial version value (default: 0)
     */
    val initialValue: Long = 0L,

    /**
     * Whether to throw an exception when optimistic lock fails
     * If false, returns 0 affected rows instead of throwing exception
     */
    val throwOnFailure: Boolean = true
)
