package com.tang.kite.logical.delete

import com.tang.kite.config.logical.LogicalDeleteConfig

/**
 * @author Tang
 */
object LogicalDeleteContext {

    private val skipFlag = ThreadLocal.withInitial { false }

    @JvmStatic
    fun isSkip(): Boolean {
        return skipFlag.get()
    }

    @JvmStatic
    fun isEnabled(): Boolean {
        return LogicalDeleteConfig.enabled
    }

    @JvmStatic
    fun shouldLogicalDelete(): Boolean {
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
    fun clear() {
        skipFlag.remove()
    }

}
