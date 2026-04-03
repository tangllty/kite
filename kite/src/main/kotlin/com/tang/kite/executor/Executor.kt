package com.tang.kite.executor

import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.SqlStatement
import java.sql.Connection

/**
 * @author Tang
 */
interface Executor : AutoCloseable {

    fun getConnection(): Connection

    fun <T> count(statement: SqlStatement, type: Class<T>): ExecutionResult<Long>

    fun <T> query(statement: SqlStatement, type: Class<T>): ExecutionResult<List<T>>

    fun update(statement: SqlStatement, parameter: Any): ExecutionResult<Int>

    fun update(batchSqlStatement: BatchSqlStatement, parameters: List<Any>): ExecutionResult<Int>

    fun commit()

    fun rollback()

    override fun close()

}
