package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.SqlNode
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

/**
 * Query where wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QueryWhereWrapper<T : Any>(

    private val queryWrapper: QueryWrapper<T>,

    whereConditions: MutableList<LogicalStatement>

) : AbstractWhereWrapper<QueryWhereWrapper<T>, T>(), QueryBuilder<T> {

    private val joinTables = mutableListOf<JoinTable>()

    private val joinWrapper = JoinWrapper(this, joinTables)

    private lateinit var whereGroupByWrapper: QueryGroupByWrapper<QueryWhereWrapper<T>, T>

    lateinit var whereHavingWrapper: QueryHavingWrapper<QueryWhereWrapper<T>, T>

    lateinit var whereOrderByWrapper: QueryOrderByWrapper<QueryWhereWrapper<T>, T>

    init {
        this.whereInstance = this
        this.conditionInstance = this
        this.wrapper = queryWrapper
        this.conditions = whereConditions
    }

    private fun isGroupByInitialized(): Boolean {
        return this::whereGroupByWrapper.isInitialized
    }

    private fun isHavingInitialized(): Boolean {
        return this::whereHavingWrapper.isInitialized
    }

    private fun isOrderByInitialized(): Boolean {
        return this::whereOrderByWrapper.isInitialized
    }

    fun join(clazz: Class<*>, joinType: JoinType): JoinWrapper<T> {
        joinWrapper.joinTables.add(JoinTable(clazz, joinType))
        return joinWrapper
    }

    /**
     * Left join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun leftJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.LEFT)
    }

    /**
     * Left join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun leftJoin(clazz: KClass<*>): JoinWrapper<T> {
        return leftJoin(clazz.java)
    }

    /**
     * Right join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun rightJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.RIGHT)
    }

    /**
     * Right join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun rightJoin(clazz: KClass<*>): JoinWrapper<T> {
        return rightJoin(clazz.java)
    }

    /**
     * Inner join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun innerJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.INNER)
    }

    /**
     * Inner join operation
     *
     * @param clazz class
     * @return JoinWrapper<T>
     */
    fun innerJoin(clazz: KClass<*>): JoinWrapper<T> {
        return innerJoin(clazz.java)
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return QueryGroupByWrapper<QueryWhereWrapper<T>, T>
     */
    fun groupBy(vararg columns: Column): QueryGroupByWrapper<QueryWhereWrapper<T>, T> {
        whereGroupByWrapper = QueryGroupByWrapper(queryWrapper, this, columns.toMutableList())
        return whereGroupByWrapper
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return QueryGroupByWrapper<QueryWhereWrapper<T>, T>
     */
    fun groupBy(vararg columns: String): QueryGroupByWrapper<QueryWhereWrapper<T>, T> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return QueryGroupByWrapper<QueryWhereWrapper<T>, T>
     */
    @SafeVarargs
    fun <E> groupBy(vararg columns: KMutableProperty1<E, *>): QueryGroupByWrapper<QueryWhereWrapper<T>, T> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Group by operation
     *
     * @param columns columns
     * @return QueryGroupByWrapper<QueryWhereWrapper<T>, T>
     */
    @SafeVarargs
    fun <E> groupBy(vararg columns: SFunction<E, *>): QueryGroupByWrapper<QueryWhereWrapper<T>, T> {
        return groupBy(*columns.map { Column(it) }.toTypedArray())
    }

    /**
     * Order by operation
     *
     * @param orderBys order by items
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    @SafeVarargs
    fun orderBy(vararg orderBys: OrderItem<*>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        whereOrderByWrapper = QueryOrderByWrapper(queryWrapper, this, orderBys.toMutableList())
        return whereOrderByWrapper
    }

    /**
     * Order by operation
     *
     * @param column column name
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun orderBy(column: String, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(OrderItem<T>(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column property
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderBy(column: KMutableProperty1<E, *>, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @param asc asc or desc
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderBy(column: SFunction<E, *>, asc: Boolean = true): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(OrderItem(column, asc))
    }

    /**
     * Order by operation
     *
     * @param column column function
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderBy(column: SFunction<E, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, true)
    }

    /**
     * Order by ascending with column name
     *
     * @param column column name
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun orderByAsc(column: String): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with column name
     *
     * @param column column name
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun orderByDesc(column: String): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with property reference
     *
     * @param column column property
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderByAsc(column: KMutableProperty1<E, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with property reference
     *
     * @param column column property
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderByDesc(column: KMutableProperty1<E, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, false)
    }

    /**
     * Order by ascending with SFunction
     *
     * @param column column function
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderByAsc(column: SFunction<E, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, true)
    }

    /**
     * Order by descending with SFunction
     *
     * @param column column function
     * @return QueryOrderByWrapper<QueryWhereWrapper<T>, T>
     */
    fun <E> orderByDesc(column: SFunction<E, *>): QueryOrderByWrapper<QueryWhereWrapper<T>, T> {
        return orderBy(column, false)
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): QueryWrapper<T> {
        this.queryWrapper.queryWhereWrapper = this
        return queryWrapper
    }

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        val list = build().baseMapper.queryWrapper(queryWrapper)
        return list.toMutableList()
    }

    fun appendSqlNode(sqlNode: SqlNode.Select) {
        sqlNode.joins.addAll(joinTables)
        if (isGroupByInitialized()) {
            whereGroupByWrapper.appendSqlNode(sqlNode.groupBy)
        }
        if (isOrderByInitialized()) {
            whereOrderByWrapper.appendSqlNode(sqlNode.orderBy)
        }
        if (isHavingInitialized()) {
            whereHavingWrapper.appendSqlNode(sqlNode.having)
        }
    }

}
