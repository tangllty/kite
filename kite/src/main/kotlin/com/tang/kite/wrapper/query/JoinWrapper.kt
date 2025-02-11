package com.tang.kite.wrapper.query

import com.tang.kite.constants.SqlString
import com.tang.kite.function.SFunction
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getColumnName
import com.tang.kite.wrapper.Column
import com.tang.kite.wrapper.enumeration.ComparisonOperator
import com.tang.kite.wrapper.enumeration.LogicalOperator
import com.tang.kite.wrapper.statement.ComparisonStatement
import com.tang.kite.wrapper.statement.LogicalStatement
import kotlin.reflect.KMutableProperty1

/**
 * @author Tang
 */
class JoinWrapper<T>(

    private val queryWhereWrapper: QueryWhereWrapper<T>,

    private val joinedClass: MutableList<Class<*>>,

    internal val joinTables: MutableList<JoinTable>

) {

    fun leftJoin(clazz: Class<*>) : JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.LEFT))
        return this
    }

    fun rightJoin(clazz: Class<*>) : JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.RIGHT))
        return this
    }

    fun innerJoin(clazz: Class<*>) : JoinWrapper<T> {
        joinedClass.add(clazz)
        joinTables.add(JoinTable(clazz, JoinType.INNER))
        return this
    }

    fun eq(left: String, right: String): JoinWrapper<T> {
        val condition = ComparisonStatement(Column(left), right, ComparisonOperator.EQUAL)
        joinTables.last().condition.add(LogicalStatement(condition, LogicalOperator.AND))
        return this
    }

    fun eq(left: KMutableProperty1<*, *>, right: KMutableProperty1<*, *>): JoinWrapper<T> {
        return eq(getColumnName(left, true), getColumnName(right, true))
    }

    fun <E1, E2> eq(left: SFunction<E1, *>, right: SFunction<E2, *>): JoinWrapper<T> {
        return eq(getColumnName(left, true), getColumnName(right, true))
    }

    fun where() : QueryWhereWrapper<T> {
        return queryWhereWrapper
    }

    fun build() : QueryWrapper<T> {
        return queryWhereWrapper.build()
    }

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        joinTables.forEach {
            sql.append(" ${it.joinType.name}${SqlString.JOIN}")
            val tableAlias = Reflects.getTableAlias(it.clazz)
            sql.append("${Reflects.getTableName(it.clazz)} $tableAlias${SqlString.ON}")

            it.condition.last().logicalOperator = null
            it.condition.forEach {
                val condition = it.condition
                sql.append("${condition.column}${condition.comparisonOperator.value}${condition.value}")
                it.logicalOperator?.let { sql.append(it.value) }
            }
        }
    }

}
