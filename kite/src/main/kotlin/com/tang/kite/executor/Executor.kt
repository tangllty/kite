package com.tang.kite.executor

import com.tang.kite.sql.SqlStatement
import java.sql.Connection

/**
 * @author Tang
 */
interface Executor : AutoCloseable {

    fun getConnection(): Connection

    fun <T> count(statement: SqlStatement, type: Class<T>): Long

    fun <T> query(statement: SqlStatement, type: Class<T>): List<T>

    fun update(statement: SqlStatement, parameter: Any): Int

    fun commit()

    fun rollback()

    override fun close()

}
