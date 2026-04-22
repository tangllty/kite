package com.tang.kite.wrapper.delete

import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Delete where wrapper for delete operation
 *
 * @author Tang
 */
class DeleteWhereWrapper<T : Any>(

    private val deleteWrapper: DeleteWrapper<T>,

    whereConditions: MutableList<LogicalStatement>

) : AbstractWhereWrapper<DeleteWhereWrapper<T>, T>(), DeleteBuilder<T> {

    init {
        this.whereInstance = this
        this.conditionInstance = this
        this.wrapper = deleteWrapper
        this.conditions = whereConditions
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
