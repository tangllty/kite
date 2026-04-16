package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.config.tenant.TenantProcessor
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Tenant properties for tenant configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = TenantProperties.TENANT_PREFIX)
open class TenantProperties (

    /**
     * Whether tenant functionality is enabled
     */
    val enabled: Boolean = TenantConfig.enabled,

    /**
     * Tenant ID field name
     */
    val fieldName: String = TenantConfig.fieldName,

    /**
     * Tenant processor for handling multiple tenant IDs
     */
    var tenantProcessor: TenantProcessor = TenantConfig.tenantProcessor

) : PropertyApplier {

    companion object {

        const val TENANT_PREFIX = KiteProperties.KITE_PREFIX + ".tenant"

    }

    override fun applyProperties() {
        TenantConfig.enabled = enabled
        TenantConfig.fieldName = fieldName
        TenantConfig.tenantProcessor = tenantProcessor
    }

}
