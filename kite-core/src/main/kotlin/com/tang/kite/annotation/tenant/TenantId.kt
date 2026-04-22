package com.tang.kite.annotation.tenant

/**
 * Tenant ID annotation for field-level configuration
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class TenantId
