package com.tang.kite.optimistic

import com.tang.kite.config.optimistic.OptimisticLockConfig

/**
 * Optimistic lock context for managing optimistic lock-related state
 *
 * @author Tang
 */
object OptimisticLockContext {

    private val skipFlag = ThreadLocal.withInitial { false }

    @JvmStatic
    fun isSkip(): Boolean {
        return skipFlag.get()
    }

    @JvmStatic
    fun isEnabled(): Boolean {
        return OptimisticLockConfig.enabled
    }

    @JvmStatic
    fun shouldApplyOptimisticLock(): Boolean {
        return isEnabled() && isSkip().not()
    }

    @JvmStatic
    fun shouldApplyOptimisticLock(clazz: Class<*>): Boolean {
        return shouldApplyOptimisticLock() && OptimisticLockConfig.optimisticLockProcessor.isTableNeedProcessing(clazz)
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
