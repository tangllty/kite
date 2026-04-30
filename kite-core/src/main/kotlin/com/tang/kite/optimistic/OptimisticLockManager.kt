package com.tang.kite.optimistic

import com.tang.kite.config.optimistic.OptimisticLockConfig
import java.util.function.Supplier

/**
 * Optimistic lock manager for executing code with optimistic lock context
 *
 * @author Tang
 */
object OptimisticLockManager {

    private fun <T> executeWithOptimisticLock(block: () -> T): T {
        if (OptimisticLockConfig.enabled) {
            return block()
        }
        OptimisticLockConfig.enabled = true
        return try {
            block()
        } finally {
            OptimisticLockConfig.enabled = false
        }
    }

    private fun <T> executeWithSkip(block: () -> T): T {
        if (OptimisticLockConfig.enabled.not()) {
            return block()
        }
        OptimisticLockContext.enableSkip()
        return try {
            block()
        } finally {
            OptimisticLockContext.disableSkip()
        }
    }

    fun withOptimisticLock(block: () -> Unit) {
        executeWithOptimisticLock(block)
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
