package com.tang.kite.wrapper.delete

/**
 * DeleteBuilder interface for delete operations
 *
 * @author Tang
 */
interface DeleteBuilder<T> {

    /**
     * Execute the delete wrapper
     *
     * @return The number of rows affected
     */
    fun delete(): Int

}
