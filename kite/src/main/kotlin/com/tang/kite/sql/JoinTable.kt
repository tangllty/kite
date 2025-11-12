package com.tang.kite.sql

import com.tang.kite.sql.enumeration.JoinType
import com.tang.kite.sql.statement.LogicalStatement

/**
 * @author Tang
 */
class JoinTable(

    val clazz: Class<*>,

    val joinType: JoinType,

    val conditions: MutableList<LogicalStatement> = mutableListOf()

)
