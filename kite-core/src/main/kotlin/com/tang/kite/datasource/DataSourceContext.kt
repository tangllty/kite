package com.tang.kite.datasource

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

}
