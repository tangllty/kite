package com.tang.kite.wrapper.update

import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Update where wrapper for update operation
 *
 * @author Tang
 */
class UpdateWhereWrapper<T>(private val updateWrapper: UpdateWrapper<T>) : AbstractWhereWrapper<UpdateWhereWrapper<T>, T, UpdateWrapper<T>>(updateWrapper, mutableListOf()), UpdateBuilder<T> {

    init {
        this.whereInstance = this
        this.conditionInstance = this
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): UpdateWrapper<T> {
        this.updateWrapper.updateWhereWrapper = this
        return updateWrapper
    }

    /**
     * Execute the update wrapper
     *
     * @return The number of rows affected
     */
    override fun update(): Int {
        return build().baseMapper.updateWrapper(updateWrapper)
    }

}
