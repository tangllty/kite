package com.tang.kite.logical

import com.tang.kite.config.logical.LogicalDeletionConfig
import java.util.function.Supplier

/**
 * @author Tang
 */
object LogicalDeletionManager {

    private fun <T> executeWithLogical(block: () -> T): T {
        if (LogicalDeletionConfig.enabled) {
            return block()
        }
        LogicalDeletionConfig.enabled = true
        return try {
            block()
        } finally {
            LogicalDeletionConfig.enabled = false
        }
    }

    private fun <T> executeWithSkip(block: () -> T): T {
        if (LogicalDeletionConfig.enabled.not()) {
            return block()
        }
        LogicalDeletionContext.enableSkip()
        return try {
            block()
        } finally {
            LogicalDeletionContext.disableSkip()
        }
    }

    fun withLogical(block: () -> Unit) {
        executeWithLogical(block)
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
