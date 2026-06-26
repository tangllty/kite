package com.tang.kite.table

/**
 * Thread-local table context for managing dynamic table name switching.
 * Uses a stack-based approach to support nested table resolution.
 *
 * @author Tang
 */
object TableContext {

    private val context = ThreadLocal.withInitial<MutableList<String>> { mutableListOf() }

    @JvmStatic
    fun push(tableName: String) {
        context.get().add(tableName)
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
