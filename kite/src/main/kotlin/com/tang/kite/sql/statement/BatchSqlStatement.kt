package com.tang.kite.sql.statement

/**
 * @author Tang
 */
class BatchSqlStatement(

    val sql: String,

    val parameters: MutableList<MutableList<Any?>>

)
