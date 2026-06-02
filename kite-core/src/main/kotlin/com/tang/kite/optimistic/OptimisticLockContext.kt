package com.tang.kite.optimistic

import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.config.optimistic.OptimisticLockConfig
import com.tang.kite.exception.OptimisticLockException
import com.tang.kite.utils.Reflects

/**
 * Optimistic lock context for managing optimistic lock-related state
 *
 * @author Tang
 */
object OptimisticLockContext {

    private val skipFlag: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

    @JvmStatic
    fun isSkip(): Boolean {
        return skipFlag.get()
    }

    @JvmStatic
    fun isEnabled(): Boolean {
        return OptimisticLockConfig.enabled || OptimisticLockManager.threadLocalEnabled.get()
    }

    @JvmStatic
    fun shouldApplyOptimisticLock(): Boolean {
        return isEnabled() && isSkip().not()
    }

    @JvmStatic
    fun shouldApplyOptimisticLock(clazz: Class<*>): Boolean {
        return shouldApplyOptimisticLock() && OptimisticLockConfig.optimisticLockProcessor.processable(clazz)
    }

    @JvmStatic
    fun <T> throwOnFailure(type: Class<T>, affectedRows: Int) {
        if (affectedRows == 0 && shouldApplyOptimisticLock(type)) {
            val versionField = Reflects.getVersionField(type)
            val throwOnFailure = if (versionField.isAnnotationPresent(Version::class.java)) {
                versionField.getAnnotation(Version::class.java).throwOnFailure
            } else {
                OptimisticLockConfig.throwOnFailure
            }
            if (throwOnFailure) {
                throw OptimisticLockException("Version conflict: update affected 0 rows, data may have been modified by another transaction")
            }
        }
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
