package com.tang.kite.datasource

import java.util.function.Supplier

/**
 * @author Tang
 */
object DataSourceContext {

    private val context = ThreadLocal.withInitial<MutableList<String>> { mutableListOf() }

    @JvmStatic
    fun push(key: String) {
        context.get().add(key)
    }

    @JvmStatic
    fun current(): String? {
        return context.get().lastOrNull()
    }

    @JvmStatic
    fun pop() {
        val stack = context.get()
        if (stack.isNotEmpty()) {
            stack.removeLast()
        }
        if (stack.isEmpty()) {
            context.remove()
        }
    }

    @JvmStatic
    fun clear() {
        context.remove()
    }

    fun <T> with(key: String, block: () -> T): T {
        try {
            push(key)
            return block()
        } finally {
            pop()
        }
    }

    @JvmStatic
    fun with(key: String, runnable: Runnable) {
        try {
            push(key)
            runnable.run()
        } finally {
            pop()
        }
    }

    @JvmStatic
    fun <T> with(key: String, supplier: Supplier<T>): T {
        try {
            push(key)
            return supplier.get()
        } finally {
            pop()
        }
    }

}

inline fun <T> withDataSource(key: String, block: () -> T): T {
    try {
        DataSourceContext.push(key)
        return block()
    } finally {
        DataSourceContext.pop()
    }
}
