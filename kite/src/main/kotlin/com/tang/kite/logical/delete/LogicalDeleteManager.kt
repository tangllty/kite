package com.tang.kite.logical.delete

import com.tang.kite.config.logical.LogicalDeleteConfig
import java.util.function.Supplier

/**
 * @author Tang
 */
object LogicalDeleteManager {

    private fun <T> executeWithLogical(block: () -> T): T {
        if (LogicalDeleteConfig.enabled) {
            return block()
        }
        LogicalDeleteConfig.enabled = true
        return try {
            block()
        } finally {
            LogicalDeleteConfig.enabled = false
        }
    }

    private fun <T> executeWithSkip(block: () -> T): T {
        if (LogicalDeleteConfig.enabled.not()) {
            return block()
        }
        LogicalDeleteContext.enableSkip()
        return try {
            block()
        } finally {
            LogicalDeleteContext.disableSkip()
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
