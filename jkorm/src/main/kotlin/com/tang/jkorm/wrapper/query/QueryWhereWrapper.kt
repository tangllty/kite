package com.tang.jkorm.wrapper.query

import com.tang.jkorm.wrapper.AbstractWhereWrapper

/**
 * @author Tang
 */
class QueryWhereWrapper<T>(private val queryWrapper: QueryWrapper<T>) : AbstractWhereWrapper<QueryWrapper<T>, List<T>>() {

    override fun build(): QueryWrapper<T> {
        this.queryWrapper.queryWhereWrapper = this
        return queryWrapper
    }

    override fun execute(): List<T> {
        val list = build().baseMapper.queryWrapper(queryWrapper)
        return list
    }

}
