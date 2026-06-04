package com.tang.kite.datasource

import java.util.function.Supplier

/**
 * Data source manager that provides the ability to switch data sources at runtime.
 *
 * @author Tang
 */
object DataSourceManager {

    /**
     * Execute the given Kotlin lambda [block] in the context of the specified data source [key].
     *
     * @param key   the data source key to switch to
     * @param block the lambda to execute under the target data source
     * @return the result produced by [block]
     */
    fun <T> with(key: String, block: () -> T): T {
        try {
            DataSourceContext.push(key)
            return block()
        } finally {
            DataSourceContext.pop()
        }
    }

    /**
     * Execute the given Java [Runnable] in the context of the specified data source [key].
     *
     * @param key      the data source key to switch to
     * @param runnable the task to run under the target data source
     */
    @JvmStatic
    fun with(key: String, runnable: Runnable) {
        try {
            DataSourceContext.push(key)
            runnable.run()
        } finally {
            DataSourceContext.pop()
        }
    }

    /**
     * Execute the given Java [Supplier] in the context of the specified data source [key]
     *
     * @param key      the data source key to switch to
     * @param supplier the supplier that provides the result under the target data source
     * @return the result provided by [supplier]
     */
    @JvmStatic
    fun <T> with(key: String, supplier: Supplier<T>): T {
        try {
            DataSourceContext.push(key)
            return supplier.get()
        } finally {
            DataSourceContext.pop()
        }
    }

}

/**
 * Top-level extension function that executes [block] in the context of the data source
 *
 * @param key   the data source key to switch to
 * @param block the lambda to execute under the target data source
 * @return the result produced by [block]
 */
inline fun <T> withDataSource(key: String, block: () -> T): T {
    try {
        DataSourceContext.push(key)
        return block()
    } finally {
        DataSourceContext.pop()
    }
}
