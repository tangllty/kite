package com.tang.jkorm.wrapper

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.sql.SqlStatement
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator
import com.tang.jkorm.wrapper.enumeration.LogicalOperator
import com.tang.jkorm.wrapper.statement.ComparisonStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Base class for where wrapper
 *
 * @author Tang
 */
abstract class AbstractWhereWrapper<out Wrapper, R> {

    private val conditions = mutableListOf<LogicalStatement>()

    fun eq(column: String, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        if (effective) {
            val condition = ComparisonStatement(column, value, ComparisonOperator.EQUAL)
            conditions.add(LogicalStatement(condition, logicalOperator))
        }
        return this
    }

    fun eq(column: String, value: Any, logicalOperator: LogicalOperator): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, logicalOperator, true)
    }

    fun eq(column: String, value: Any, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun eq(column: String, value: Any): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        if (effective) {
            var filed = column.javaField!!
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, logicalOperator, true)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        if (effective) {
            val filed = Fields.getField(column)
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, logicalOperator, true)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, effective: Boolean): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any): AbstractWhereWrapper<Wrapper, R> {
        return eq(column, value, LogicalOperator.AND)
    }

    abstract fun build(): Wrapper

    abstract fun execute(): R

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        if (conditions.isEmpty()) {
            return
        }
        sql.append(WHERE)
        conditions.last().logicalOperator = null
        conditions.forEach { it.appendSql(sql, parameters) }
    }

    fun getSqlStatement(): SqlStatement {
        val sql: StringBuilder = StringBuilder()
        val parameters: MutableList<Any?> = mutableListOf()
        appendSql(sql, parameters)
        return SqlStatement(JkOrmConfig.INSTANCE.getSql(sql), parameters)
    }

}
