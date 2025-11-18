package com.tang.kite.sql

import com.tang.kite.constants.SqlString
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.statement.LogicalStatement
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class JoinTable(

    val joinType: JoinType,

    val table: TableReference,

    val conditions: MutableList<LogicalStatement> = mutableListOf(),

    @Deprecated("Use table instead")
    val clazz: Class<*>

) {
    fun toString(withAlias: Boolean): String {
        if (conditions.isEmpty()) {
            throw IllegalArgumentException("JoinTable must have conditions")
        }
        val conditionsSql = StringBuilder()
        conditions.last().logicalOperator = null
        conditions.forEach { logicalStatement ->
            val condition = logicalStatement.condition
            conditionsSql.append("${condition.column}${condition.comparisonOperator.value}${condition.value}")
            logicalStatement.logicalOperator?.let { conditionsSql.append(it.value) }
        }
        return "${joinType.sqlSymbol}${table.toString(withAlias)}${SqlString.ON} $conditionsSql"
    }

    @Deprecated("Remove in future versions")
    constructor(clazz: Class<*>, joinType: JoinType, conditions: MutableList<LogicalStatement> = mutableListOf()) : this(
        joinType = joinType,
        table = TableReference(clazz),
        clazz = clazz,
        conditions = conditions
    )

    @Deprecated("Remove in future versions")
    constructor(joinType: JoinType, clazz: Class<*>, conditions: MutableList<LogicalStatement> = mutableListOf()) : this(
        joinType = joinType,
        table = TableReference(clazz),
        clazz = clazz,
        conditions = conditions.toMutableList()
    )

    @Deprecated("Remove in future versions")
    constructor(joinType: JoinType, clazz: KClass<*>, conditions: MutableList<LogicalStatement> = mutableListOf()) : this(
        joinType = joinType,
        table = TableReference(clazz),
        clazz = clazz.java,
        conditions = conditions.toMutableList()
    )

}
