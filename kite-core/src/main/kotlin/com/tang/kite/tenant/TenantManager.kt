package com.tang.kite.tenant

import com.tang.kite.config.tenant.TenantConfig
import java.util.function.Supplier

/**
 * Tenant manager for executing code with tenant context
 *
 * @author Tang
 */
object TenantManager {

    private fun <T> executeWithTenant(block: () -> T): T {
        if (TenantConfig.enabled) {
            return block()
        }
        TenantConfig.enabled = true
        return try {
            block()
        } finally {
            TenantConfig.enabled = false
        }
    }

    private fun <T> executeWithSkip(block: () -> T): T {
        if (TenantConfig.enabled.not()) {
            return block()
        }
        TenantContext.enableSkip()
        return try {
            block()
        } finally {
            TenantContext.disableSkip()
        }
    }

    fun withTenant(block: () -> Unit) {
        executeWithTenant(block)
    }

    fun <T> withTenant(block: () -> T): T {
        return executeWithTenant(block)
    }

    @JvmStatic
    fun withTenant(block: Runnable) {
        executeWithTenant { block.run() }
    }

    @JvmStatic
    fun <T> withTenant(block: Supplier<T>): T {
        return executeWithTenant { block.get() }
    }

    fun withSkip(block: () -> Unit) {
        executeWithSkip(block)
    }

    fun <T> withSkip(block: () -> T): T {
        return executeWithSkip(block)
    }

    @JvmStatic
    fun withSkip(block: Runnable) {
        executeWithSkip { block.run() }
    }

    @JvmStatic
    fun <T> withSkip(block: Supplier<T>): T {
        return executeWithSkip { block.get() }
    }

}
