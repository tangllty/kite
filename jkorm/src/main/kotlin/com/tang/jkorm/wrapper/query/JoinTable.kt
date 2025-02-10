package com.tang.jkorm.wrapper.query

import com.tang.jkorm.wrapper.statement.LogicalStatement

/**
 * @author Tang
 */
class JoinTable(

    val clazz: Class<*>,

    val joinType: JoinType,

    val condition: MutableList<LogicalStatement> = mutableListOf()

)
