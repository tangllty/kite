package com.tang.kite.wrapper.update

/**
 * UpdateBuilder interface for update operations
 *
 * @author Tang
 */
interface UpdateBuilder<T> {

    /**
     * Execute the update wrapper
     *
     * @return The number of rows affected
     */
    fun update(): Int

}
