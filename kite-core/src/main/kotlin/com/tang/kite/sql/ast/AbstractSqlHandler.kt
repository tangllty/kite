package com.tang.kite.sql.ast

import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.tenant.TenantConfig
import com.tang.kite.constants.SqlString
import com.tang.kite.enumeration.SqlType
import com.tang.kite.exception.OptimisticLockException
import com.tang.kite.logical.LogicalDeletionContext
import com.tang.kite.optimistic.OptimisticLockContext
import com.tang.kite.sql.Column
import com.tang.kite.sql.JoinTable
import com.tang.kite.sql.TableReference
import com.tang.kite.sql.enumeration.ComparisonOperator
import com.tang.kite.sql.enumeration.LogicalOperator
import com.tang.kite.sql.statement.ComparisonStatement
import com.tang.kite.sql.statement.LogicalStatement
import com.tang.kite.tenant.TenantContext
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * @author Tang
 */
open class AbstractSqlHandler {

    internal fun appendColumns(columns: MutableList<Column>, from: TableReference?, joins: MutableList<JoinTable>) {
        if (columns.isNotEmpty()) {
            return
        }
        val joinedClasses = joins.map { it.table.clazz }.toSet()
        if (from == null) {
            throw IllegalArgumentException("Table reference can not be null")
        }
        listOf(from.clazz).plus(joinedClasses).forEach {
            if (it == null) {
                throw IllegalArgumentException("Table reference can not be null")
            }
            val fields = Reflects.getSqlFields(it)
            fields.forEach { field ->
                columns.add(Column(field))
            }
        }
    }

    internal fun appendTable(sql: StringBuilder, from: TableReference?, withAlias: Boolean = false) {
        sql.append(from?.toString(withAlias) ?: throw IllegalArgumentException("Table reference can not be null"))
    }

    internal fun appendJoins(joins: MutableList<JoinTable>, sql: StringBuilder, withAlias: Boolean) {
        if (joins.isEmpty()) {
            return
        }
        joins.forEach { sql.append(it.toString(withAlias)) }
    }

    internal fun appendWhere(sql: StringBuilder, parameters: MutableList<Any?>, where: MutableList<LogicalStatement>, withAlias: Boolean) {
        if (where.isEmpty()) {
            return
        }
        sql.append(SqlString.WHERE)
        if (where.last().nestedConditions.isEmpty()) {
            where.last().logicalOperator = null
        }
        where.forEach { it.appendSql(sql, parameters, withAlias) }
    }

    internal fun appendWhere(sql: StringBuilder, table: TableReference?, parameters: MutableList<Any?>, where: MutableList<LogicalStatement>, sqlType: SqlType, withAlias: Boolean) {
        if (table == null) {
            throw IllegalArgumentException("Table reference can not be null")
        }
        if (shouldApplyLogicalDeletion(table)) {
            val logicalField = Reflects.getLogicalField(table.clazz!!)
            val logicalValue = LogicalDeletionConfig.logicalDeletionProcessor.process(logicalField)
            val comparisonStatement = ComparisonStatement(Column(logicalField), logicalValue.normalValue)
            where.add(LogicalStatement(comparisonStatement, LogicalOperator.AND))
        }
        if (shouldApplyTenant(table)) {
            val tenantField = Reflects.getTenantField(table.clazz!!)
            val tenantIds = if (sqlType == SqlType.SELECT) {
                TenantConfig.tenantProcessor.getTenantIds(tenantField)
            } else {
                listOf(TenantConfig.tenantProcessor.getFirstTenantId(tenantField))
            }
            val tenantValue = if (tenantIds.size <= 1) tenantIds.firstOrNull() else tenantIds
            val comparisonOperator = if (tenantIds.size > 1) ComparisonOperator.IN else ComparisonOperator.EQUAL
            val comparisonStatement = ComparisonStatement(Column(tenantField), tenantValue, comparisonOperator)
            where.add(LogicalStatement(comparisonStatement, LogicalOperator.AND))
        }
        appendWhere(sql, parameters, where, withAlias)
    }

    internal fun appendInsertPrefix(sql: StringBuilder, table: TableReference?, columns: MutableList<Column>) {
        sql.append(SqlString.INSERT_INTO)
        appendTable(sql, table)
        sql.append(SqlString.SPACE + SqlString.LEFT_BRACKET)
        sql.append(columns.joinToString(SqlString.COMMA_SPACE) { it.toString() })
        sql.append(SqlString.RIGHT_BRACKET + SqlString.VALUES)
    }

    internal fun applyInsertColumn(insert: SqlNode.Insert, field: Field, value: Any?) {
        val (_, columns, valuesList) = insert
        val column = columns.find { it.name == Reflects.getColumnName(field) }
        if (column != null) {
            val index = columns.indexOf(column)
            columns.removeAt(index)
            valuesList.forEach { it.removeAt(index) }
        }
        columns.add(Column(field))
        valuesList.forEach { it.add(value) }
    }

    internal fun applyInsertLogical(insert: SqlNode.Insert) {
        val table = insert.table
        val clazz = table?.clazz!!
        if (shouldApplyLogicalDeletion(table)) {
            val logicalField = Reflects.getLogicalField(clazz)
            val logicalValue = LogicalDeletionConfig.logicalDeletionProcessor.process(logicalField)
            applyInsertColumn(insert, logicalField, logicalValue.normalValue)
        }
    }

    internal fun applyInsertTenant(insert: SqlNode.Insert) {
        val table = insert.table
        val clazz = table?.clazz!!
        if (shouldApplyTenant(table)) {
            val tenantField = Reflects.getTenantField(clazz)
            val tenantId = TenantConfig.tenantProcessor.getFirstTenantId(tenantField)
            applyInsertColumn(insert, tenantField, tenantId)
        }
    }

    internal fun applyInsertOptimisticLock(insert: SqlNode.Insert) {
        val table = insert.table
        val clazz = table?.clazz!!
        if (shouldApplyOptimisticLock(table)) {
            val versionField = Reflects.getVersionField(clazz)
            val annotation = versionField.getAnnotation(Version::class.java)
            val initialValue = annotation.initialValue
            applyInsertColumn(insert, versionField, initialValue)
        }
    }
    internal fun applyUpdateRemoveOptimisticLockColumn(update: SqlNode.Update) {
        val table = update.table
        val clazz = table?.clazz!!
        if (shouldApplyOptimisticLock(table)) {
            val versionField = Reflects.getVersionField(clazz)
            val versionColumn = Column(versionField)
            val column = update.sets.keys.find { it.name.equals(versionColumn.name) }
            if (column != null) {
                update.sets.remove(column)
            }
        }
    }

    internal fun applyUpdateOptimisticLock(sql: StringBuilder, withAlias: Boolean, update: SqlNode.Update) {
        val table = update.table
        val clazz = table?.clazz!!
        if (shouldApplyOptimisticLock(table)) {
            val versionField = Reflects.getVersionField(clazz)
            val versionColumn = Column(versionField)
            val hasVersionCondition = update.where.any { it.condition.column.name.equals(versionColumn.name) }
            if (!hasVersionCondition) {
                throw OptimisticLockException("Version condition not found in WHERE clause")
            }
            val columnName = versionColumn.toString(withAlias)
            sql.append(", $columnName = $columnName + 1")
        }
    }

    internal fun shouldApplyLogicalDeletion(table: TableReference?): Boolean {
        return LogicalDeletionContext.shouldLogicalDeletion() && LogicalDeletionConfig.logicalDeletionProcessor.isTableNeedProcessing(table?.clazz!!)
    }

    internal fun shouldApplyTenant(table: TableReference?): Boolean {
        return TenantContext.shouldApplyTenant() && TenantConfig.tenantProcessor.isTableNeedProcessing(table?.clazz!!)
    }

    internal fun shouldApplyOptimisticLock(table: TableReference?): Boolean {
        return OptimisticLockContext.shouldApplyOptimisticLock(table?.clazz!!)
    }

}
