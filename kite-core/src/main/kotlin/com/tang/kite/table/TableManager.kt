package com.tang.kite.table

import com.tang.kite.utils.Reflects
import java.util.function.Function
import java.util.function.Supplier
import kotlin.reflect.KClass

/**
 * Dynamic table name context manager.
 *
 * Provides thread-safe table name switching capability, allowing temporary database table name
 * switching during business logic execution. Maintains a thread-local table name stack through
 * [TableContext] to ensure proper restoration during nested calls.
 *
 * @author Tang
 */
object TableManager {

    /**
     * Execute business logic block under the specified table name context.
     *
     * @param tableName Target table name
     * @param block Business logic lambda expression
     * @return Result returned by the block
     */
    fun <R> with(tableName: String, block: () -> R): R {
        return withTable(tableName, block)
    }

    /**
     * Java-compatible overload - Execute without return value.
     *
     * @param tableName Target table name
     * @param runnable Java Runnable task
     */
    @JvmStatic
    fun with(tableName: String, runnable: Runnable) {
        withTable(tableName) { runnable.run() }
    }

    /**
     * Java-compatible overload - Execute and get return value via Supplier.
     *
     * @param tableName Target table name
     * @param supplier Java Supplier to obtain execution result
     * @return Result provided by the supplier
     */
    @JvmStatic
    fun <R> with(tableName: String, supplier: Supplier<R>): R {
        return withTable(tableName) { supplier.get() }
    }

    /**
     * Append custom suffix to entity table name and execute logic block.
     *
     * Resolves the base table name from the entity class via reflection, concatenates the suffix,
     * and switches the context. Generated table name format: `baseTableName_suffix`
     *
     * @param entityClazz Entity class to resolve base table name
     * @param suffix Suffix string appended after the table name
     * @param block Business logic lambda expression
     * @return Result returned by the block
     */
    fun <R, T> withSuffix(entityClazz: Class<T>, suffix: String, block: () -> R): R {
        return withTableSuffix(entityClazz, suffix, block)
    }

    /**
     * Kotlin KClass overload for withSuffix.
     *
     * @param entityClazz Entity KClass to resolve base table name
     * @param suffix Suffix string appended after the table name
     * @param block Business logic lambda expression
     * @return Result returned by the block
     */
    fun <R, T : Any> withSuffix(entityClazz: KClass<T>, suffix: String, block: () -> R): R {
        return withSuffix(entityClazz.java, suffix, block)
    }

    /**
     * Java-compatible overload of withSuffix - No return value task.
     *
     * @param entityClazz Entity class to resolve base table name
     * @param suffix Suffix string appended after the table name
     * @param runnable Java Runnable task
     */
    @JvmStatic
    fun <T> withSuffix(entityClazz: Class<T>, suffix: String, runnable: Runnable) {
        withSuffix(entityClazz, suffix) { runnable.run() }
    }

    /**
     * Java-compatible overload of withSuffix - Task with return value via Supplier.
     *
     * @param entityClazz Entity class to resolve base table name
     * @param suffix Suffix string appended after the table name
     * @param supplier Java Supplier to obtain execution result
     * @return Result provided by the supplier
     */
    @JvmStatic
    fun <R, T> withSuffix(entityClazz: Class<T>, suffix: String, supplier: Supplier<R>): R {
        return withSuffix(entityClazz, suffix) { supplier.get() }
    }

    /**
     * Transform entity table name with custom transformer and execute logic block.
     *
     * Allows fully customizable table name transformation logic, suitable for complex
     * table name mapping scenarios.
     *
     * @param entityClazz Entity class to resolve raw table name
     * @param tableNameTransformer Lambda that receives original table name and returns new table name
     * @param block Business logic lambda expression
     * @return Result returned by the block
     */
    fun <R, T: Any> withTransform(entityClazz: Class<T>, tableNameTransformer: (tableName: String) -> String, block: () -> R): R {
        return withTableTransform(entityClazz, tableNameTransformer, block)
    }

    /**
     * Kotlin KClass overload for withTransform.
     *
     * @param entityClazz Entity KClass to resolve raw table name
     * @param tableNameTransformer Lambda that receives original table name and returns new table name
     * @param block Business logic lambda expression
     * @return Result returned by the block
     */
    fun <R, T : Any> withTransform(entityClazz: KClass<T>, tableNameTransformer: (tableName: String) -> String, block: () -> R): R {
        return withTransform(entityClazz.java, tableNameTransformer, block)
    }

    /**
     * Java-compatible overload of withTransform - No return value task, generates table name via Supplier.
     *
     * @param entityClazz Entity class to resolve raw table name
     * @param tableNameTransformer Java Function to produce final table name
     * @param runnable Java Runnable task
     */
    @JvmStatic
    fun <T> withTransform(entityClazz: Class<T>, tableNameTransformer: Function<String, String>, runnable: Runnable) {
        withTableTransform(entityClazz, { tableNameTransformer.apply(it) }) { runnable.run() }
    }

    /**
     * Java-compatible overload of withTransform - Generates table name via Supplier, task with return value.
     *
     * @param entityClazz Entity class to resolve raw table name
     * @param tableNameTransformer Java Function to produce final table name
     * @param supplier Java Supplier to obtain business execution result
     * @return Result provided by the supplier
     */
    @JvmStatic
    fun <R, T> withTransform(entityClazz: Class<T>, tableNameTransformer: Function<String, String>, supplier: Supplier<R>): R {
        return withTableTransform(entityClazz, { tableNameTransformer.apply(it) }) { supplier.get() }
    }

}

/**
 * Executes [block] in the context of the specified table name.
 *
 * Internally uses try-finally to ensure proper context cleanup after execution completes,
 * supporting nested calls.
 *
 * @param tableName Target table name to push into context
 * @param block Lambda executed under the target table context
 * @return Return value of the block
 */
inline fun <R> withTable(tableName: String, block: () -> R): R {
    try {
        TableContext.push(tableName)
        return block()
    } finally {
        TableContext.pop()
    }
}

/**
 * Executes [block] with specified table name suffix.
 *
 * Resolves the table name from the entity class via [Reflects.getTableName], then concatenates the suffix.
 *
 * @param entityClazz Entity class to resolve original table name
 * @param suffix Suffix string appended to original table name
 * @param block Lambda executed under the suffixed table context
 * @return Return value of the block
 */
inline fun <R, T> withTableSuffix(entityClazz: Class<T>, suffix: String, block: () -> R): R {
    val tableName = Reflects.getTableName(entityClazz)
    return withTable("${tableName}_$suffix", block)
}

/**
 * Kotlin KClass overload - Executes [block] with specified table name suffix.
 *
 * @param entityClazz Entity KClass to resolve table name
 * @param suffix Suffix string appended to original table name
 * @param block Lambda executed under the suffixed table context
 * @return Return value of the block
 */
inline fun <R, T : Any> withTableSuffix(entityClazz: KClass<T>, suffix: String, block: () -> R): R {
    return withTableSuffix(entityClazz.java, suffix, block)
}

/**
 * Executes [block] with dynamically transformed table name.
 *
 * Uses the provided transformer function to convert the original table name, suitable for
 * scenarios requiring flexible table name mapping.
 *
 * @param entityClazz Entity class to resolve raw table name
 * @param tableNameTransformer Lambda that receives original table name and returns new table name
 * @param block Lambda executed under the transformed table context
 * @return Return value of the block
 */
inline fun <R, T> withTableTransform(entityClazz: Class<T>, tableNameTransformer: (tableName: String) -> String, block: () -> R): R {
    val tableName = tableNameTransformer(Reflects.getTableName(entityClazz))
    return withTable(tableName, block)
}

/**
 * Kotlin KClass overload - Executes [block] with dynamically transformed table name.
 *
 * @param entityClazz Entity KClass to resolve raw table name
 * @param tableNameTransformer Lambda that receives original table name and returns new table name
 * @param block Lambda executed under the transformed table context
 * @return Return value of the block
 */
inline fun <R, T : Any> withTableTransform(entityClazz: KClass<T>, tableNameTransformer: (tableName: String) -> String, block: () -> R): R {
    return withTableTransform(entityClazz.java, tableNameTransformer, block)
}
