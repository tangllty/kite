package com.tang.jkorm.wrapper.query

import com.tang.jkorm.wrapper.where.AbstractWhereWrapper

/**
 * Query where wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QueryWhereWrapper<T>(private val queryWrapper: QueryWrapper<T>) : AbstractWhereWrapper<T, List<T>, QueryWrapper<T>>(mutableListOf()) {

    /**
     * Build the query
     */
    override fun build(): QueryWrapper<T> {
        this.queryWrapper.queryWhereWrapper = this
        return queryWrapper
    }

    /**
     * Execute the query
     */
    override fun execute(): List<T> {
        val list = build().baseMapper.queryWrapper(queryWrapper)
        return list
    }

}
