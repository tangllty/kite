package com.tang.kite.sql

import com.tang.kite.constants.SqlString
import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.statement.LogicalStatement
import kotlin.reflect.KClass

/**
 * @author Tang
 */
class JoinTable(

    val table: TableReference,

    val joinType: JoinType,

    val conditions: MutableList<LogicalStatement> = mutableListOf()

) {

    constructor(clazz: Class<*>, joinType: JoinType, conditions: MutableList<LogicalStatement> = mutableListOf()) : this(
        TableReference(clazz), joinType, conditions
    )

    constructor(clazz: KClass<*>, joinType: JoinType, conditions: MutableList<LogicalStatement> = mutableListOf()) : this(
        TableReference(clazz), joinType, conditions
    )

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
        return "${joinType.sqlSymbol}${table.toString(withAlias)}${SqlString.ON}$conditionsSql"
    }

}
