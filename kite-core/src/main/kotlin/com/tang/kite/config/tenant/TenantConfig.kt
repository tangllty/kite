package com.tang.kite.config.tenant

import com.tang.kite.config.defaults.DefaultTenantProcessor

/**
 * Tenant configuration properties for tenant functionality
 *
 * @author Tang
 */
object TenantConfig {

    /**
     * Whether tenant functionality is enabled
     */
    @JvmStatic
    var enabled: Boolean = false

    /**
     * Tenant ID field name
     */
    @JvmStatic
    var fieldName: String = "tenantId"

    /**
     * Tenant processor for handling multiple tenant IDs
     */
    @JvmStatic
    var tenantProcessor: TenantProcessor = DefaultTenantProcessor

}
