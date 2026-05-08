package com.tang.kite.sql.ast.ddl

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

        sql.append("create table ")
        val tableName = createTableNode.table?.toString(false) ?: throw IllegalArgumentException("Table name cannot be null")
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
            sql.append(" comment = '${createTableNode.comment}'")
        }
        sqlList.add(sql.toString())

        createTableNode.createIndexes.forEach { createIndexNode ->
            sqlList.addAll(handleCreateIndex(createIndexNode, dialect))
        }

        if (createTableNode.comment != null && dialect.supportsCommentOnTable()) {
            sqlList.add("comment on table ${createTableNode.table?.toString(false)} is '${createTableNode.comment}'")
        }

        createTableNode.columns.forEach { column ->
            if (column.comment != null && dialect.supportsCommentOnColumn()) {
                sqlList.add(getColumnComment(column, dialect))
            }
        }
        return sqlList
    }

    override fun handleAlterTable(alterTableNode: SqlNode.AlterTable, dialect: SqlDialect): List<String> {
        val sqlList = mutableListOf<String>()
        val commentList = mutableListOf<String>()
        val tableName = alterTableNode.table?.toString(false) ?: throw IllegalArgumentException("Table name cannot be null")

        alterTableNode.operations.forEach { operation ->
            val sql = StringBuilder()
            sql.append("alter table ").append(tableName).append(" ")

            when (operation) {
                is AlterOperation.AddColumn -> {
                    sql.append(dialect.getAddColumnKeyword()).append(" ")
                    appendColumnMeta(sql, operation.column, dialect)
                    commentList.add(getColumnComment(operation.column, dialect))
                }
                is AlterOperation.DropColumn -> {
                    sql.append(dialect.getDropColumnKeyword())
                    sql.append(" ${operation.columnName}")
                    if (operation.cascade && dialect.supportsCascade()) {
                        sql.append(" cascade")
                    }
                }
                is AlterOperation.ModifyColumn -> {
                    sql.append(dialect.getAlterColumnKeyword()).append(" ")
                    appendColumnMeta(sql, operation.column, dialect)
                    commentList.add(getColumnComment(operation.column, dialect))
                }
                is AlterOperation.RenameColumn -> {
                    sql.append("rename column ${operation.oldName} to ${operation.newName}")
                }
                is AlterOperation.AddConstraint -> {
                    sql.append("add ")
                    appendTableConstraint(sql, operation.constraint)
                }
                is AlterOperation.DropConstraint -> {
                    sql.append("drop constraint ${operation.constraintName}")
                    if (operation.cascade && dialect.supportsCascade()) {
                        sql.append(" cascade")
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
        val tableName = dropTableNode.table?.toString(false) ?: throw IllegalArgumentException("Table name cannot be null")

        sql.append("drop table ")
        sql.append(tableName)

        if (dropTableNode.cascade && dialect.supportsCascade()) {
            sql.append(" cascade")
        }

        return listOf(sql.toString())
    }

    override fun handleCreateIndex(createIndexNode: SqlNode.CreateIndex, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()

        sql.append("create ")

        if (createIndexNode.unique) {
            sql.append("unique ")
        }

        sql.append("index ")

        sql.append(createIndexNode.indexName)
        sql.append(" on ")
        sql.append(createIndexNode.table.toString(false))
        sql.append(" (")
        sql.append(createIndexNode.columns.joinToString(", "))
        sql.append(")")

        return listOf(sql.toString())
    }

    override fun handleDropIndex(dropIndexNode: SqlNode.DropIndex, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()

        sql.append("drop index ")

        sql.append(dropIndexNode.indexName ?: "")

        if (dropIndexNode.table != null && dialect.requiresTableForDropIndex()) {
            sql.append(" on ")
            sql.append(dropIndexNode.table.toString())
        }

        return listOf(sql.toString())
    }

    override fun handleTruncateTable(truncateTableNode: SqlNode.TruncateTable, dialect: SqlDialect): List<String> {
        val sql = StringBuilder()

        sql.append("truncate table ")
        sql.append(truncateTableNode.table?.toString(false))

        if (truncateTableNode.cascade && dialect.supportsCascade()) {
            sql.append(" cascade")
        }

        return listOf(sql.toString())
    }

    private fun appendColumnMeta(sql: StringBuilder, column: ColumnMeta, dialect: SqlDialect) {
        sql.append(column.columnName)
        sql.append(" ")
        sql.append(getType(column))

        if (column.nullable.not()) {
            sql.append(" not null")
        }

        if (column.defaultValue.isNullOrBlank().not()) {
            sql.append(" default ${column.defaultValue}")
        }

        if (column.autoIncrement) {
            sql.append(" ")
            sql.append(dialect.getAutoIncrementKeyword())
        }

        if (column.primaryKey) {
            sql.append(" primary key")
        }

        if (column.unique) {
            sql.append(" unique")
        }

        if (column.comment != null && dialect.supportsCommentOnColumn().not()) {
            sql.append(" comment '${column.comment}'")
        }
    }

    private fun appendTableConstraint(sql: StringBuilder, constraint: TableConstraint) {
        when (constraint) {
            is TableConstraint.PrimaryKey -> {
                if (constraint.name != null) {
                    sql.append("constraint ${constraint.name} ")
                }
                sql.append("primary key (${constraint.columns.joinToString(", ")})")
            }
            is TableConstraint.Unique -> {
                if (constraint.name != null) {
                    sql.append("constraint ${constraint.name} ")
                }
                sql.append("unique (${constraint.columns.joinToString(", ")})")
            }
            is TableConstraint.ForeignKey -> {
                if (constraint.name != null) {
                    sql.append("constraint ${constraint.name} ")
                }
                sql.append("foreign key (${constraint.columns.joinToString(", ")}) ")
                sql.append("references ${constraint.referencedTable} (${constraint.referencedColumns.joinToString(", ")})")

                // Add foreign key actions if specified
                if (constraint.onDelete != null) {
                    sql.append(" on delete ${constraint.onDelete.name.lowercase().replace("_", " ")}")
                }
                if (constraint.onUpdate != null) {
                    sql.append(" on update ${constraint.onUpdate.name.lowercase().replace("_", " ")}")
                }
            }
            is TableConstraint.Check -> {
                if (constraint.name != null) {
                    sql.append("constraint ${constraint.name} ")
                }
                sql.append("check (${constraint.expression})")
            }
        }
    }

    private fun getType(column: ColumnMeta): String {
        val sql = StringBuilder()
        sql.append(column.typeName)
        when {
            column.typeName.startsWith("decimal") || column.typeName.startsWith("numeric") -> {
                if (column.columnSize > 0 && column.decimalDigits >= 0) {
                    sql.append("(${column.columnSize}, ${column.decimalDigits})")
                } else {
                    sql.append("(10, 2)")
                }
            }
            column.typeName.startsWith("varchar") || column.typeName.startsWith("char") -> {
                if (column.columnSize > 0) {
                    sql.append("(${column.columnSize})")
                } else {
                    sql.append("(255)")
                }
            }
        }
        return sql.toString()
    }

    fun getColumnComment(column: ColumnMeta, dialect: SqlDialect): String {
        val tableName = column.tableName
        val columnName = column.columnName
        val sql = if (dialect.supportsCommentOnColumn()) {
            "comment on column $tableName.$columnName is '${column.comment ?: ""}'"
        } else {
            if (column.comment == null) {
                "drop comment on column $tableName.$columnName"
            } else {
                "alter table $tableName modify column $columnName ${getType(column)} comment '${column.comment}'"
            }
        }
        return sql
    }

}
