package com.tang.kite.sql.ast.ddl

import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect

/**
 * DDL handler interface for processing DDL operations
 *
 * @author Tang
 */
interface DdlHandler<T> {

    fun handleCreateTable(createTableNode: SqlNode.CreateTable, dialect: SqlDialect): T

    fun handleAlterTable(alterTableNode: SqlNode.AlterTable, dialect: SqlDialect): T

    fun handleDropTable(dropTableNode: SqlNode.DropTable, dialect: SqlDialect): T

    fun handleCreateIndex(createIndexNode: SqlNode.CreateIndex, dialect: SqlDialect): T

    fun handleDropIndex(dropIndexNode: SqlNode.DropIndex, dialect: SqlDialect): T

    fun handleTruncateTable(truncateTableNode: SqlNode.TruncateTable, dialect: SqlDialect): T

}
