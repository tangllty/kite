package com.tang.kite.wrapper.query

import com.tang.kite.wrapper.where.AbstractWhereWrapper

/**
 * Query where wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QueryWhereWrapper<T>(private val queryWrapper: QueryWrapper<T>) : AbstractWhereWrapper<T, List<T>, QueryWrapper<T>>(queryWrapper, mutableListOf()) {

    private val joinedClass = mutableListOf<Class<*>>()

    private val joinTables = mutableListOf<JoinTable>()

    private val joinWrapper = JoinWrapper(this, joinedClass, joinTables)

    private var multiTableQuery = false

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

    internal fun isMultiTableQuery(): Boolean {
        return multiTableQuery
    }

    fun getJoinedClass() : List<Class<*>> {
        return joinedClass
    }

    fun leftJoin(clazz: Class<*>) : JoinWrapper<T> {
        multiTableQuery = true
        joinedClass.add(clazz)
        joinWrapper.joinTables.add(JoinTable(clazz, JoinType.LEFT))
        return joinWrapper
    }

    override fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, multiTableQuery: Boolean) {
        joinWrapper.appendSql(sql, parameters)
        super.appendSql(sql, parameters, multiTableQuery)
    }

}
