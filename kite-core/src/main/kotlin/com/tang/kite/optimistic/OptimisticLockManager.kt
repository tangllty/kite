package com.tang.kite.optimistic

import com.tang.kite.config.optimistic.OptimisticLockConfig
import java.util.function.Supplier

/**
 * Optimistic lock manager for executing code with optimistic lock context
 *
 * @author Tang
 */
object OptimisticLockManager {

    @JvmStatic
    internal var threadLocalEnabled: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

    private fun <T> executeWithOptimisticLock(block: () -> T): T {
        if (OptimisticLockConfig.enabled || threadLocalEnabled.get()) {
            return block()
        }
        threadLocalEnabled.set(true)
        return try {
            block()
        } finally {
            threadLocalEnabled.remove()
        }
    }

    private fun <T> executeWithSkip(block: () -> T): T {
        if (OptimisticLockConfig.enabled.not() && threadLocalEnabled.get().not()) {
            return block()
        }
        OptimisticLockContext.enableSkip()
        return try {
            block()
        } finally {
            OptimisticLockContext.disableSkip()
        }
    }

    fun <T> withOptimisticLock(block: () -> T): T {
        return executeWithOptimisticLock(block)
    }

    @JvmStatic
    fun withOptimisticLock(block: Runnable) {
        executeWithOptimisticLock { block.run() }
    }

    @JvmStatic
    fun <T> withOptimisticLock(block: Supplier<T>): T {
        return executeWithOptimisticLock { block.get() }
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
