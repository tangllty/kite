package com.tang.jkorm.wrapper.update

import com.tang.jkorm.wrapper.AbstractWhereWrapper

/**
 * Update where wrapper for update operation
 *
 * @author Tang
 */
class UpdateWhereWrapper<T>(private val updateWrapper: UpdateWrapper<T>) : AbstractWhereWrapper<UpdateWrapper<T>, Int>() {

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
