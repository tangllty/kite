package com.tang.kite.wrapper.update

import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Update where wrapper for update operation
 *
 * @author Tang
 */
class UpdateWhereWrapper<T>(private val updateWrapper: UpdateWrapper<T>) : AbstractWhereWrapper<T, Int, UpdateWrapper<T>>(updateWrapper, mutableListOf()) {

    /**
     * Build the update
     *
     * @return UpdateWrapper
     */
    override fun build(): UpdateWrapper<T> {
        this.updateWrapper.updateWhereWrapper = this
        return updateWrapper
    }

    /**
     * Execute the update
     *
     * @return the number of rows affected
     */
    override fun execute(): Int {
        return build().baseMapper.updateWrapper(updateWrapper)
    }

}
