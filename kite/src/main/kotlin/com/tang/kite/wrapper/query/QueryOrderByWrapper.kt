package com.tang.kite.wrapper.query

import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.where.AbstractOrderByWrapper

/**
 * @author Tang
 */
class QueryOrderByWrapper<R, T, W>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: QueryWhereWrapper<T>,

    columns: MutableList<OrderItem<*>> = mutableListOf()

) : AbstractOrderByWrapper<QueryOrderByWrapper<R, T, W>, T, W>(wrapper, columns), QueryBuilder<T> {

    init {
        this.orderByInstance = this
    }

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        return whereWrapper.build().baseMapper.selectWrapper(wrapper as QueryWrapper<T>).toMutableList()
    }

}
