package com.tang.jkorm.executor

import java.sql.Connection

/**
 * @author Tang
 */
interface Executor {

    fun getConnection(): Connection

    fun <T> count(statement: String, type: Class<T>): Long

    fun <T> query(statement: String, type: Class<T>): List<T>

    fun update(statement: String, parameter: Any): Int

    fun commit()

    fun rollback()

    fun close()

}
