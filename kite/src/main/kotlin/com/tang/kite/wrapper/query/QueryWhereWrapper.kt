package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.statement.LogicalStatement
import com.tang.kite.wrapper.where.AbstractWhereWrapper
import kotlin.reflect.KMutableProperty1

/**
 * Query where wrapper for [QueryWrapper]
 *
 * @author Tang
 */
class QueryWhereWrapper<T>(

    private val queryWrapper: QueryWrapper<T>,

    whereConditions: MutableList<LogicalStatement>

) : AbstractWhereWrapper<QueryWhereWrapper<T>, T>(), QueryBuilder<T> {

    private val joinedClass = mutableListOf<Class<*>>()

    private val joinTables = mutableListOf<JoinTable>()

    private val joinWrapper = JoinWrapper(this, joinedClass, joinTables)

    private var multiTableQuery = false

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

    fun rightJoin(clazz: Class<*>) : JoinWrapper<T> {
        multiTableQuery = true
        joinedClass.add(clazz)
        joinWrapper.joinTables.add(JoinTable(clazz, JoinType.RIGHT))
        return joinWrapper
    }

    fun innerJoin(clazz: Class<*>) : JoinWrapper<T> {
        multiTableQuery = true
        joinedClass.add(clazz)
        joinWrapper.joinTables.add(JoinTable(clazz, JoinType.INNER))
        return joinWrapper
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
        val list = build().baseMapper.selectWrapper(queryWrapper)
        return list.toMutableList()
    }

    override fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>, multiTableQuery: Boolean) {
        joinWrapper.appendSql(sql, parameters)
        super.appendSql(sql, parameters, multiTableQuery)
        if (isGroupByInitialized()) {
            whereGroupByWrapper.appendSql(sql, multiTableQuery)
        }
        if (isHavingInitialized()) {
            whereHavingWrapper.appendSql(sql, parameters, multiTableQuery)
        }
        if (isOrderByInitialized()) {
            whereOrderByWrapper.appendSql(sql, multiTableQuery)
        }
    }

}
