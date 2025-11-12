package com.tang.kite.executor

import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.sql.statement.SqlStatement
import java.sql.Connection

/**
 * @author Tang
 */
interface Executor : AutoCloseable {

    fun getConnection(): Connection

    fun <T> count(statement: SqlStatement, type: Class<T>): Long

    fun <T> query(statement: SqlStatement, type: Class<T>): List<T>

    fun update(statement: SqlStatement, parameter: Any): Int

    fun update(batchSqlStatement: BatchSqlStatement, parameters: List<Any>): Int

    fun update(statements: List<SqlStatement>, parameters: List<Any>): Int

    fun commit()

    fun rollback()

    override fun close()

}
