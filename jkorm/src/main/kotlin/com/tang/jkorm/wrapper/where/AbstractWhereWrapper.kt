package com.tang.jkorm.wrapper.where

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import com.tang.jkorm.wrapper.where.comparison.AbstractComparisonWrapper

/**
 * Base class for where wrapper
 *
 * @author Tang
 */
abstract class AbstractWhereWrapper<T, R, W>(private val conditions: MutableList<LogicalStatement>) : AbstractComparisonWrapper<T, R, W>(conditions) {

    /**
     * Get the SQL statement
     *
     * @return SqlStatement
     */
    fun getSqlStatement(): SqlStatement {
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        appendSql(sql, parameters)
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

    open fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        if (conditions.isEmpty()) {
            return
        }
        sql.append(WHERE)
        if (conditions.last().nestedConditions.isEmpty()) {
            conditions.last().logicalOperator = null
        }
        conditions.forEach {
            it.appendSql(sql, parameters)
        }
    }

}
