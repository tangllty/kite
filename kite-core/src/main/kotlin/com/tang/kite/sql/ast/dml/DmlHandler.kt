package com.tang.kite.sql.ast.dml

import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect

/**
 * @author Tang
 */
interface DmlHandler<T> {

    fun handleSelect(selectNode: SqlNode.Select, dialect: SqlDialect?): T

    fun handleInsert(insertNode: SqlNode.Insert): T

    fun handleUpdate(updateNode: SqlNode.Update): T

    fun handleDelete(deleteNode: SqlNode.Delete): T

}
