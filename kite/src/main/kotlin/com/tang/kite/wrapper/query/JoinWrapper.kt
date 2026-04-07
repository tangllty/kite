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

    /**
     * Generic join operation
     *
     * @param clazz the entity class to join
     * @param joinType the type of join to perform
     * @return JoinWrapper<T> for chaining operations
     */
    fun join(clazz: Class<*>, joinType: JoinType): JoinWrapper<T> {
        joinTables.add(JoinTable(clazz, joinType))
        return this
    }

    /**
     * Generic join operation
     *
     * @param clazz the entity class to join
     * @param joinType the type of join to perform
     * @return JoinWrapper<T> for chaining operations
     */
    fun join(clazz: KClass<*>, joinType: JoinType): JoinWrapper<T> {
        joinTables.add(JoinTable(clazz.java, joinType))
        return this
    }

    /**
     * Left join operation
     *
     * @param clazz the entity class to left join
     * @return JoinWrapper<T> for chaining operations
     */
    fun leftJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.LEFT)
    }

    /**
     * Left join operation using Kotlin class
     *
     * @param clazz the entity class to left join
     * @return JoinWrapper<T> for chaining operations
     */
    fun leftJoin(clazz: KClass<*>): JoinWrapper<T> {
        return leftJoin(clazz.java)
    }

    /**
     * Right join operation
     *
     * @param clazz the entity class to right join
     * @return JoinWrapper<T> for chaining operations
     */
    fun rightJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.RIGHT)
    }

    /**
     * Right join operation using Kotlin class
     *
     * @param clazz the entity class to right join
     * @return JoinWrapper<T> for chaining operations
     */
    fun rightJoin(clazz: KClass<*>): JoinWrapper<T> {
        return rightJoin(clazz.java)
    }

    /**
     * Inner join operation
     *
     * @param clazz the entity class to inner join
     * @return JoinWrapper<T> for chaining operations
     */
    fun innerJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.INNER)
    }

    /**
     * Inner join operation using Kotlin class
     *
     * @param clazz the entity class to inner join
     * @return JoinWrapper<T> for chaining operations
     */
    fun innerJoin(clazz: KClass<*>): JoinWrapper<T> {
        return innerJoin(clazz.java)
    }

    /**
     * Full join operation
     *
     * @param clazz the entity class to full join
     * @return JoinWrapper<T> for chaining operations
     */
    fun fullJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.FULL)
    }

    /**
     * Full join operation using Kotlin class
     *
     * @param clazz the entity class to full join
     * @return JoinWrapper<T> for chaining operations
     */
    fun fullJoin(clazz: KClass<*>): JoinWrapper<T> {
        return fullJoin(clazz.java)
    }

    /**
     * Cross join operation
     *
     * @param clazz the entity class to cross join
     * @return JoinWrapper<T> for chaining operations
     */
    fun crossJoin(clazz: Class<*>): JoinWrapper<T> {
        return join(clazz, JoinType.CROSS)
    }

    /**
     * Cross join operation using Kotlin class
     *
     * @param clazz the entity class to cross join
     * @return JoinWrapper<T> for chaining operations
     */
    fun crossJoin(clazz: KClass<*>): JoinWrapper<T> {
        return crossJoin(clazz.java)
    }

    /**
     * On condition operation
     *
     * @param left left column
     * @param right right column
     * @return JoinWrapper<T> for chaining operations
     */
    fun on(left: String, right: String): JoinWrapper<T> {
        val condition = ComparisonStatement(Column(left), right, ComparisonOperator.EQUAL)
        joinTables.last().conditions.add(LogicalStatement(condition, LogicalOperator.AND))
        return this
    }

    /**
     * On condition operation
     *
     * @param left left column
     * @param right right column
     * @return JoinWrapper<T> for chaining operations
     */
    fun on(left: KMutableProperty1<*, *>, right: KMutableProperty1<*, *>): JoinWrapper<T> {
        return on(getColumnName(left, true), getColumnName(right, true))
    }

    /**
     * On condition operation
     *
     * @param left left column
     * @param right right column
     * @return JoinWrapper<T> for chaining operations
     */
    fun <E1, E2> on(left: SFunction<E1, *>, right: SFunction<E2, *>): JoinWrapper<T> {
        return on(getColumnName(left, true), getColumnName(right, true))
    }

    /**
     * Where condition operation
     *
     * @return QueryWhereWrapper<T> for chaining operations
     */
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
