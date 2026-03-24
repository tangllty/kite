package com.tang.kite.tenant

import com.tang.kite.config.tenant.TenantConfig

/**
 * Tenant context for managing tenant-related state
 *
 * @author Tang
 */
object TenantContext {

    private val skipFlag = ThreadLocal.withInitial { false }
    private val currentTenantId = ThreadLocal<String?>()

    @JvmStatic
    fun isSkip(): Boolean {
        return skipFlag.get()
    }

    @JvmStatic
    fun isEnabled(): Boolean {
        return TenantConfig.enabled
    }

    @JvmStatic
    fun shouldApplyTenant(): Boolean {
        return isEnabled() && isSkip().not()
    }

    @JvmStatic
    fun enableSkip() {
        skipFlag.set(true)
    }

    @JvmStatic
    fun disableSkip() {
        skipFlag.set(false)
    }

    @JvmStatic
    fun getCurrentTenantId(): String? {
        return currentTenantId.get()
    }

    @JvmStatic
    fun setCurrentTenantId(tenantId: String?) {
        currentTenantId.set(tenantId)
    }

    @JvmStatic
    fun clear() {
        skipFlag.remove()
        currentTenantId.remove()
    }

}
