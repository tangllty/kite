package com.tang.kite.wrapper.query

import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.constants.SqlString
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getColumnName
import com.tang.kite.sql.Column
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.wrapper.where.WrapperBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
class JoinWrapper<T : Any>(

    private val queryWhereWrapper: QueryWhereWrapper<T>,

    private val joinedClass: MutableList<Class<*>>,

    internal val joinTables: MutableList<JoinTable>

) : WrapperBuilder<T>, QueryBuilder<T> {

    fun leftJoin(clazz: Class<*>): JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.LEFT))
        return this
    }

    fun leftJoin(clazz: KClass<*>): JoinWrapper<T> {
        return leftJoin(clazz.java)
    }

    fun rightJoin(clazz: Class<*>): JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.RIGHT))
        return this
    }

    fun rightJoin(clazz: KClass<*>): JoinWrapper<T> {
        return rightJoin(clazz.java)
    }

    fun innerJoin(clazz: Class<*>): JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.INNER))
        return this
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

    override fun build(): QueryWrapper<T> {
        return queryWhereWrapper.build()
    }

    override fun list(): MutableList<T> {
        return queryWhereWrapper.list()
    }

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        joinTables.forEach { table ->
            sql.append(" ${table.joinType.name}${SqlString.JOIN}")
            val tableAlias = Reflects.getTableAlias(table.clazz)
            sql.append("${Reflects.getTableName(table.clazz)} $tableAlias${SqlString.ON}")

            table.conditions.last().logicalOperator = null
            table.conditions.forEach { logicalStatement ->
                val condition = logicalStatement.condition
                sql.append("${condition.column}${condition.comparisonOperator.value}${condition.value}")
                logicalStatement.logicalOperator?.let { sql.append(it.value) }
            }
        }
    }

}
