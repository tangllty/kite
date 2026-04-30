package com.tang.kite.sql.ast.dml

import com.tang.kite.config.SqlConfig
import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.constants.SqlString
import com.tang.kite.enumeration.SqlType
import com.tang.kite.sql.ast.AbstractSqlHandler
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.statement.BatchSqlStatement
import com.tang.kite.utils.Reflects

/**
 * @author Tang
 */
object BatchSqlStatementDmlHandler : AbstractSqlHandler(), DmlHandler<BatchSqlStatement> {

    override fun handleInsert(insertNode: SqlNode.Insert): BatchSqlStatement {
        val (table, columns, valuesList) = insertNode
        val sql = StringBuilder()
        applyInsertLogical(insertNode)
        applyInsertTenant(insertNode)
        applyInsertOptimisticLock(insertNode)
        appendInsertPrefix(sql, table, columns)
        sql.append(SqlString.LEFT_BRACKET)
        sql.append(valuesList.first().joinToString(SqlString.COMMA_SPACE) { SqlString.QUESTION_MARK })
        sql.append(SqlString.RIGHT_BRACKET)
        return BatchSqlStatement(SqlConfig.getSql(sql), valuesList)
    }

    override fun handleUpdate(updateNode: SqlNode.Update): BatchSqlStatement {
        val (table, sets, valuesList, joins, where) = updateNode
        val sql = StringBuilder()
        val withAlias = joins.isNotEmpty()
        sql.append(SqlString.UPDATE)
        appendTable(sql, table)
        sql.append(SqlString.SET)
        applyUpdateRemoveOptimisticLockColumn(updateNode)
        sql.append(sets.entries.joinToString(SqlString.COMMA_SPACE) {
            "${it.key.toString(withAlias)} = ${SqlString.QUESTION_MARK}"
        })
        applyUpdateOptimisticLock(sql, withAlias, updateNode)
        appendJoins(joins, sql, withAlias)
        if (shouldApplyLogicalDeletion(table)) {
            val logicalField = Reflects.getLogicalField(table?.clazz!!)
            val logicalValue = LogicalDeletionConfig.logicalDeletionProcessor.process(logicalField)
            valuesList.forEach { it.add(logicalValue.normalValue) }
        }
        appendWhere(sql, table, mutableListOf(), where, SqlType.UPDATE, withAlias)
        return BatchSqlStatement(SqlConfig.getSql(sql), valuesList)
    }

    override fun handleDelete(deleteNode: SqlNode.Delete): BatchSqlStatement {
        throw UnsupportedOperationException("BatchSqlStatement does not support delete statement")
    }

}
