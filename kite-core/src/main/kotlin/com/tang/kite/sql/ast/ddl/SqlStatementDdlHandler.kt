package com.tang.kite.sql.ast.ddl

import com.tang.kite.config.schema.SchemaConfig.getSql
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.sql.ast.AlterOperation
import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.ast.TableConstraint
import com.tang.kite.sql.dialect.SqlDialect

/**
 * DDL handler that generates SQL statements
 *
 * @author Tang
 */
object SqlStatementDdlHandler : DdlHandler<List<String>> {

    override fun handleCreateTable(createTableNode: SqlNode.CreateTable, dialect: SqlDialect): List<String> {
        val sqlList = mutableListOf<String>()
        val sql = StringBuilder()

        sql.append(getSql("create table "))
        val tableName = createTableNode.table.toString()
        sql.append(tableName)
        sql.append(" (")

        createTableNode.columns.forEachIndexed { index, column ->
            if (index > 0) sql.append(", ")
            appendColumnMeta(sql, column, dialect)
        }

        createTableNode.constraints.forEach { constraint ->
            sql.append(", ")
            appendTableConstraint(sql, constraint)
        }
        sql.append(")")

        if (createTableNode.comment != null && dialect.supportsCommentOnTable().not()) {
            sql.append(" ${getSql("comment")} = '${createTableNode.comment}'")
        }
        sqlList.add(sql.toString())

        createTableNode.createIndexes.forEach { createIndexNode ->
            sqlList.addAll(handleCreateIndex(createIndexNode, dialect))
        }

        if (createTableNode.comment != null && dialect.supportsCommentOnTable()) {
            sqlList.add("${getSql("comment on table")} ${createTableNode.table} ${getSql("is")} '${createTableNode.comment}'")
        }

        createTableNode.columns.forEach { column ->
            if (column.comment != null && dialect.supportsCommentOnColumn()) {
                sqlList.add(getColumnComment(tableName, column, dialect))
            }
        }
        return sqlList
    }

    override fun handleAlterTable(alterTableNode: SqlNode.AlterTable, dialect: SqlDialect): List<String> {
        val sqlList = mutableListOf<String>()
        val commentList = mutableListOf<String>()
        val tableName = alterTableNode.table.toString()

        alterTableNode.operations.forEach { operation ->
            val sql = StringBuilder()
            sql.append(getSql("alter table ")).append(tableName).append(" ")

            when (operation) {
                is AlterOperation.AddColumn -> {
                    sql.append(getSql(dialect.getAddColumnKeyword())).append(" ")
                    appendColumnMeta(sql, operation.column, dialect)
                    commentList.add(getColumnComment(tableName, operation.column, dialect))
                }
                is AlterOperation.DropColumn -> {
                    sql.append(getSql(dialect.getDropColumnKeyword()))
                    sql.append(" ${operation.columnName}")
                    if (operation.cascade && dialect.supportsCascade()) {
                        sql.append(getSql(" cascade"))
                    }
                }
                is AlterOperation.ModifyColumn -> {
                    sql.append(getSql(dialect.getAlterColumnKeyword())).append(" ")
                    appendColumnMeta(sql, operation.column, dialect)
                    commentList.add(getColumnComment(tableName, operation.column, dialect))
                }
                is AlterOperation.RenameColumn -> {
                    sql.append(getSql("rename column "))
                    sql.append("${operation.oldName} ${getSql("to")} ${operation.newName}")
                }
                is AlterOperation.AddConstraint -> {
                    sql.append(getSql("add "))
                    appendTableConstraint(sql, operation.constraint)
                }
                is AlterOperation.DropConstraint -> {
                    sql.append(getSql("drop constraint "))
                    sql.append(operation.constraintName)
                    if (operation.cascade && dialect.supportsCascade()) {
                        sql.append(getSql(" cascade"))
                    }
                }
            }
            sqlList.add(sql.toString())
        }
        sqlList.addAll(commentList)
        return sqlList
    }

    override fun handleDropTable(dropTableNode: SqlNode.DropTable, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()
        val tableName = dropTableNode.table.toString()
        sql.append(getSql("drop table "))
        sql.append(tableName)
        if (dropTableNode.cascade && dialect.supportsCascade()) {
            sql.append(getSql(" cascade"))
        }
        return listOf(sql.toString())
    }

    override fun handleCreateIndex(createIndexNode: SqlNode.CreateIndex, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()
        sql.append(getSql("create "))
        if (createIndexNode.unique) {
            sql.append(getSql("unique "))
        }
        sql.append(getSql("index "))
        sql.append(createIndexNode.indexName)
        sql.append(getSql(" on "))
        sql.append(createIndexNode.table.toString())
        sql.append(" (")
        val columns = createIndexNode.columns.mapIndexed { index, string ->
            val order = createIndexNode.sorts.getOrNull(index)
            if (order != null) "$string ${getSql(order.name)}" else string
        }
        sql.append(columns.joinToString(", "))
        sql.append(")")
        return listOf(sql.toString())
    }

    override fun handleDropIndex(dropIndexNode: SqlNode.DropIndex, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()
        sql.append(getSql("drop index "))
        sql.append(dropIndexNode.indexName)
        if (dropIndexNode.table != null && dialect.requiresTableForDropIndex()) {
            sql.append(getSql(" on "))
            sql.append(dropIndexNode.table.toString())
        }
        return listOf(sql.toString())
    }

    override fun handleTruncateTable(truncateTableNode: SqlNode.TruncateTable, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()
        sql.append(getSql("truncate table "))
        sql.append(truncateTableNode.table.toString())
        if (truncateTableNode.cascade && dialect.supportsCascade()) {
            sql.append(getSql(" cascade"))
        }
        return listOf(sql.toString())
    }

    private fun appendColumnMeta(sql: StringBuilder, column: ColumnMeta, dialect: SqlDialect) {
        sql.append(column.columnName)
        sql.append(" ")
        sql.append(getType(column))
        if (column.nullable.not()) {
            sql.append(getSql(" not null"))
        }
        if (column.defaultValue.isNullOrBlank().not()) {
            sql.append(getSql(" default "))
            sql.append(column.defaultValue)
        }
        if (column.autoIncrement) {
            sql.append(" ")
            sql.append(dialect.getAutoIncrementKeyword())
        }
        if (column.primaryKey) {
            sql.append(getSql(" primary key"))
        }
        if (column.unique) {
            sql.append(getSql(" unique"))
        }
        if (column.comment != null && dialect.supportsCommentOnColumn().not()) {
            sql.append(getSql(" comment "))
            sql.append("'${column.comment}'")
        }
    }

    private fun appendTableConstraint(sql: StringBuilder, constraint: TableConstraint) {
        when (constraint) {
            is TableConstraint.PrimaryKey -> {
                if (constraint.name != null) {
                    sql.append("${getSql("constraint")} ${constraint.name} ")
                }
                sql.append("${getSql("primary key")} (${constraint.columns.joinToString(", ")})")
            }
            is TableConstraint.Unique -> {
                if (constraint.name != null) {
                    sql.append("${getSql("constraint")} ${constraint.name} ")
                }
                sql.append("${getSql("unique")} (${constraint.columns.joinToString(", ")})")
            }
            is TableConstraint.ForeignKey -> {
                if (constraint.name != null) {
                    sql.append("${getSql("constraint")} ${constraint.name} ")
                }
                sql.append("${getSql("foreign key")} (${constraint.columns.joinToString(", ")}) ")
                sql.append("${getSql("references")} ${constraint.referencedTable} (${constraint.referencedColumns.joinToString(", ")})")

                // Add foreign key actions if specified
                if (constraint.onDelete != null) {
                    sql.append(getSql(" on delete "))
                    sql.append(constraint.onDelete.name.lowercase().replace("_", " "))
                }
                if (constraint.onUpdate != null) {
                    sql.append(getSql(" on update "))
                    sql.append(constraint.onUpdate.name.lowercase().replace("_", " "))
                }
            }
            is TableConstraint.Check -> {
                if (constraint.name != null) {
                    sql.append("${getSql("constraint")} ${constraint.name} ")
                }
                sql.append("${getSql("check")} (${constraint.expression})")
            }
        }
    }

    private fun getType(column: ColumnMeta): String {
        val sql = StringBuilder()
        sql.append(getSql(column.typeName))
        when {
            column.typeName.startsWith("decimal", ignoreCase = true) || column.typeName.startsWith("numeric", ignoreCase = true) -> {
                if (column.columnSize > 0 && column.decimalDigits >= 0) {
                    sql.append("(${column.columnSize}, ${column.decimalDigits})")
                } else {
                    sql.append("(10, 2)")
                }
            }
            column.typeName.startsWith("varchar", ignoreCase = true) || column.typeName.startsWith("char", ignoreCase = true) -> {
                if (column.columnSize > 0) {
                    sql.append("(${column.columnSize})")
                } else {
                    sql.append("(255)")
                }
            }
        }
        return sql.toString()
    }

    fun getColumnComment(tableName: String, column: ColumnMeta, dialect: SqlDialect): String {
        val columnName = column.columnName
        val sql = if (dialect.supportsCommentOnColumn()) {
            "${getSql("comment on column")} $tableName.$columnName ${getSql("is")} '${column.comment ?: ""}'"
        } else {
            if (column.comment == null) {
                "${getSql("drop comment on column")} $tableName.$columnName"
            } else {
                "${getSql("alter table")} $tableName ${getSql("modify column")} $columnName ${getType(column)} ${getSql("comment")} '${column.comment}'"
            }
        }
        return sql
    }

}
