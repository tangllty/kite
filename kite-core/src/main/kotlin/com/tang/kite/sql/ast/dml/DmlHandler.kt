package com.tang.kite.sql.ast.dml

import com.tang.kite.sql.ast.SqlNode

/**
 * @author Tang
 */
interface DmlHandler<T> {

    fun handleInsert(insertNode: SqlNode.Insert): T

    fun handleUpdate(updateNode: SqlNode.Update): T

    fun handleDelete(deleteNode: SqlNode.Delete): T

}
