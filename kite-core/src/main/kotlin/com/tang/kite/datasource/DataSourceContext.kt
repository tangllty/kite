package com.tang.kite.datasource

/**
 * Thread-local data source context for managing dynamic data source switching.
 * Uses a stack-based approach to support nested data source resolution.
 *
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

}
