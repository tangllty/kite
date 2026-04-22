package com.tang.kite.wrapper.update

import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Update where wrapper for update operation
 *
 * @author Tang
 */
class UpdateWhereWrapper<T : Any>(

    private val updateWrapper: UpdateWrapper<T>,

    whereConditions: MutableList<LogicalStatement>

) : AbstractWhereWrapper<UpdateWhereWrapper<T>, T>(), UpdateBuilder<T> {

    init {
        this.whereInstance = this
        this.conditionInstance = this
        this.wrapper = updateWrapper
        this.conditions = whereConditions
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
