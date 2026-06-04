package com.tang.kite.logical

import com.tang.kite.config.logical.LogicalDeletionConfig
import java.util.function.Supplier

/**
 * Manager for controlling logical deletion behavior at runtime.
 * Allows executing code blocks with logical deletion explicitly enabled or skipped.
 *
 * @author Tang
 */
object LogicalDeletionManager {

    @JvmStatic
    internal var threadLocalEnabled: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

    private fun <T> executeWithLogical(block: () -> T): T {
        if (LogicalDeletionConfig.enabled || threadLocalEnabled.get()) {
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
        if (LogicalDeletionConfig.enabled.not() && threadLocalEnabled.get().not()) {
            return block()
        }
        LogicalDeletionContext.enableSkip()
        return try {
            block()
        } finally {
            LogicalDeletionContext.disableSkip()
        }
    }

    fun <T> withLogical(block: () -> T): T {
        return executeWithLogical(block)
    }

    @JvmStatic
    fun withLogical(block: Runnable) {
        executeWithLogical { block.run() }
    }

    @JvmStatic
    fun <T> withLogical(block: Supplier<T>): T {
        return executeWithLogical { block.get() }
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
