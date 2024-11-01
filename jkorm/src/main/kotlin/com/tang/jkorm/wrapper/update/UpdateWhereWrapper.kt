package com.tang.jkorm.wrapper.update

import com.tang.jkorm.constants.SqlString.WHERE
import com.tang.jkorm.function.SFunction
import com.tang.jkorm.utils.Fields
import com.tang.jkorm.utils.Reflects
import com.tang.jkorm.wrapper.enumeration.ComparisonOperator
import com.tang.jkorm.wrapper.enumeration.LogicalOperator
import com.tang.jkorm.wrapper.statement.ComparisonStatement
import com.tang.jkorm.wrapper.statement.LogicalStatement
import java.util.LinkedList
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Update where wrapper for update operation
 *
 * @author Tang
 */
class UpdateWhereWrapper(val updateWrapper: UpdateWrapper) {

    private val conditions = LinkedList<LogicalStatement>()

    fun eq(column: String, value: Any, logicalOperator: LogicalOperator, effective: Boolean): UpdateWhereWrapper {
        if (effective) {
            val condition = ComparisonStatement(column, value, ComparisonOperator.EQUAL)
            conditions.add(LogicalStatement(condition, logicalOperator))
        }
        return this
    }

    fun eq(column: String, value: Any, logicalOperator: LogicalOperator): UpdateWhereWrapper {
        return eq(column, value, logicalOperator, true)
    }

    fun eq(column: String, value: Any, effective: Boolean): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun eq(column: String, value: Any): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): UpdateWhereWrapper {
        if (effective) {
            var filed = column.javaField!!
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, logicalOperator: LogicalOperator): UpdateWhereWrapper {
        return eq(column, value, logicalOperator, true)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any, effective: Boolean): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun <T> eq(column: KMutableProperty1<T, *>, value: Any): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator, effective: Boolean): UpdateWhereWrapper {
        if (effective) {
            val filed = Fields.getField(column)
            return eq(Reflects.getColumnName(filed), value, logicalOperator, true)
        }
        return this
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, logicalOperator: LogicalOperator): UpdateWhereWrapper {
        return eq(column, value, logicalOperator, true)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any, effective: Boolean): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND, effective)
    }

    fun <T> eq(column: SFunction<T, *>, value: Any): UpdateWhereWrapper {
        return eq(column, value, LogicalOperator.AND)
    }

    fun build(): UpdateWrapper {
        this.updateWrapper.updateWhereWrapper = this
        return updateWrapper
    }

    fun execute(): Int {
        return build().baseMapper.update(updateWrapper)
    }

    fun appendSql(sql: StringBuilder, parameters: MutableList<Any?>) {
        sql.append(WHERE)
        conditions.last().logicalOperator = null
        conditions.forEach { it.appendSql(sql, parameters) }
    }

}
