package com.tang.kite.wrapper.query

import com.tang.kite.function.SFunction
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.utils.Reflects.getColumnName
import com.tang.kite.wrapper.where.WrapperBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

/**
 * Join wrapper for building JOIN clauses in SELECT statements
 *
 * @author Tang
 */
class JoinWrapper<T : Any>(

    private val queryWhereWrapper: QueryWhereWrapper<T>,

    internal val joinTables: MutableList<JoinTable>

) : WrapperBuilder<T>, QueryBuilder<T> {

    fun join(clazz: Class<*>, joinType: JoinType): JoinWrapper<T> {
        joinTables.add(JoinTable(clazz, joinType))
        return this
    }

    fun leftJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.LEFT)
    }

    fun leftJoin(clazz: KClass<*>): JoinWrapper<T> {
        return leftJoin(clazz.java)
    }

    fun rightJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.RIGHT)
    }

    fun rightJoin(clazz: KClass<*>): JoinWrapper<T> {
        return rightJoin(clazz.java)
    }

    fun innerJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.INNER)
    }

    fun innerJoin(clazz: KClass<*>): JoinWrapper<T> {
        return innerJoin(clazz.java)
    }

    fun on(left: String, right: String): JoinWrapper<T> {
        val condition = ComparisonStatement(Column(left), right, ComparisonOperator.EQUAL)
        joinTables.last().conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        return this
    }

    fun on(left: KMutableProperty1<*, *>, right: KMutableProperty1<*, *>): JoinWrapper<T> {
        return on(getColumnName(left, true), getColumnName(right, true))
    }

    fun <E1, E2> on(left: SFunction<E1, *>, right: SFunction<E2, *>): JoinWrapper<T> {
        return on(getColumnName(left, true), getColumnName(right, true))
    }

    fun where(): QueryWhereWrapper<T> {
        return queryWhereWrapper
    }

    /**
     * Build the wrapper
     *
     * @return Wrapper instance
     */
    override fun build(): QueryWrapper<T> {
        return queryWhereWrapper.build()
    }

    /**
     * Execute the query wrapper and return a list of results
     *
     * @return List of results
     */
    override fun list(): MutableList<T> {
        return queryWhereWrapper.list()
    }

    /**
     * Execute the count wrapper and return the count of results
     *
     * @return Count of results
     */
    override fun count(): Long {
        return queryWhereWrapper.count()
    }

}
