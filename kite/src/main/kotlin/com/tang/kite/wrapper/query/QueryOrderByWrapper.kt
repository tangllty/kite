package com.tang.kite.wrapper.query

import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.wrapper.where.AbstractOrderByWrapper

/**
 * @author Tang
 */
class QueryOrderByWrapper<R, T : Any>(

    private val wrapper: Wrapper<T>,

    private val whereWrapper: QueryWhereWrapper<T>,

    columns: MutableList<OrderItem<*>> = mutableListOf()

) : AbstractOrderByWrapper<QueryOrderByWrapper<R, T>, T>(wrapper, columns), QueryBuilder<T> {

    init {
        this.orderByInstance = this
    }

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        return whereWrapper.build().baseMapper.queryWrapper(wrapper as QueryWrapper<T>).toMutableList()
    }

}
