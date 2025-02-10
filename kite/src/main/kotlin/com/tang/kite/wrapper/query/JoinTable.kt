package com.tang.kite.wrapper.query

import com.tang.kite.wrapper.statement.LogicalStatement

/**
 * @author Tang
 */
class JoinTable(

    val clazz: Class<*>,

    val joinType: JoinType,

    val condition: MutableList<LogicalStatement> = mutableListOf()

)
