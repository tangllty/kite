package com.tang.kite.wrapper.delete

import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Delete where wrapper for delete operation
 *
 * @author Tang
 */
class DeleteWhereWrapper<T>(private val deleteWrapper: DeleteWrapper<T>) : AbstractWhereWrapper<DeleteWhereWrapper<T>, T>(deleteWrapper, mutableListOf()), DeleteBuilder<T> {

    init {
        this.whereInstance = this
        this.conditionInstance = this
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): DeleteWrapper<T> {
        this.deleteWrapper.deleteWhereWrapper = this
        return deleteWrapper
    }

    /**
     * Execute the delete wrapper
     *
     * @return The number of rows affected
     */
    override fun delete(): Int {
        return build().baseMapper.deleteWrapper(deleteWrapper)
    }

}
