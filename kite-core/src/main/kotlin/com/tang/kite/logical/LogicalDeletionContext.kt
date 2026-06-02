package com.tang.kite.logical

import com.tang.kite.config.logical.LogicalDeletionConfig

/**
 * @author Tang
 */
object LogicalDeletionContext {

    private val skipFlag: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

    @JvmStatic
    fun isSkip(): Boolean {
        return skipFlag.get()
    }

    @JvmStatic
    fun isEnabled(): Boolean {
        return LogicalDeletionConfig.enabled || LogicalDeletionManager.threadLocalEnabled.get()
    }

    @JvmStatic
    fun shouldApplyLogicalDeletion(): Boolean {
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
