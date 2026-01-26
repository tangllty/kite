package com.tang.kite.wrapper.query

/**
 * QueryBuilder interface for query operations
 *
 * @author Tang
 */
interface QueryBuilder<T : Any> {

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    fun list(): MutableList<T>

    /**
     * Execute the query wrapper and return a single result
     *
     * @return Single result or null if not found
     * @throws IllegalArgumentException if more than one result is found
     */
    fun one(): T? {
        val list = list()
        if (list.size > 1) {
            throw IllegalArgumentException("Too many results, expected one, but got ${list.size}")
        }
        return list.firstOrNull()
    }

    /**
     * Execute the query wrapper and return a single result
     *
     * @return Single result or null if not found
     */
    fun first(): T? {
        return list().firstOrNull()
    }

    /**
     * Execute the query wrapper and return the last result
     *
     * @return Last result or null if not found
     */
    fun last(): T? {
        return list().lastOrNull()
    }

    /**
     * Execute the count wrapper and return the count of results
     *
     * @return Count of results
     */
    fun count(): Long

}
