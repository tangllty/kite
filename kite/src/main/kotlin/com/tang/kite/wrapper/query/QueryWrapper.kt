package com.tang.kite.wrapper.query

import com.tang.kite.config.SqlConfig
import com.tang.kite.constants.SqlString.SELECT
import com.tang.kite.constants.SqlString.SELECT_DISTINCT
import com.tang.kite.function.SFunction
import com.tang.kite.mapper.BaseMapper
import com.tang.kite.sql.statement.SqlStatement
import com.tang.kite.sql.Column
import com.tang.kite.wrapper.Wrapper
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KMutableProperty1

/**
 * Build a select query
 *
 * @author Tang
 */
class QueryWrapper<T : Any> : AbstractWhereWrapper<QueryWhereWrapper<T>, T>, Wrapper<T> {

    private var distinct: Boolean = false

    lateinit var baseMapper: BaseMapper<T>

    lateinit var querySelectWrapper: QuerySelectWrapper<T>

    lateinit var queryWhereWrapper: QueryWhereWrapper<T>

    constructor()

    constructor(baseMapper: BaseMapper<T>) {
        this.baseMapper = baseMapper
    }

    override fun initialize(conditions: MutableList<LogicalStatement>) {
        querySelectWrapper = QuerySelectWrapper(this, mutableListOf())
        queryWhereWrapper = QueryWhereWrapper(this, conditions)
        this.conditionInstance = queryWhereWrapper
    }

    companion object {

        /**
         * Create a new QueryWrapper instance
         */
        @JvmStatic
        fun <T : Any> create(): QueryWrapper<T> {
            return QueryWrapper()
        }

    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun select(vararg columns: Column): QuerySelectWrapper<T> {
        this.querySelectWrapper = QuerySelectWrapper(this, columns.toMutableList())
        return querySelectWrapper
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    fun select(vararg columns: String): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Select without columns
     *
     * @return QuerySelectWrapper
     */
    fun select(): QuerySelectWrapper<T> {
        return select(*mutableListOf<Column>().toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: SFunction<T, *>): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Set the select columns
     *
     * @param columns columns
     * @return QuerySelectWrapper
     */
    @SafeVarargs
    fun select(vararg columns: KMutableProperty1<T, *>): QuerySelectWrapper<T> {
        return select(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Make the select columns distinct
     *
     * @return QueryWrapper
     */
    fun distinct(): QueryWrapper<T> {
        this.distinct = true
        return this
    }

    override fun getSqlStatement(): SqlStatement {
        checkValues()
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        sql.append(if (distinct) SELECT_DISTINCT else SELECT)
        val isMultiTableQuery = queryWhereWrapper.isMultiTableQuery()
        val joinedClass = queryWhereWrapper.getJoinedClass()
        querySelectWrapper.appendSql(sql, joinedClass, isMultiTableQuery)
        queryWhereWrapper.appendSql(sql, parameters, isMultiTableQuery)
        return SqlStatement(SqlConfig.getSql(sql), parameters)
    }

    override fun checkValues() {
    }

}
