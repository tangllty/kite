package com.tang.kite.sql

/**
 * @author Tang
 */
class BatchSqlStatement(

    val sql: String,

    val parameters: MutableList<MutableList<Any?>>

)
