package com.tang.kite.sql.ast.dql

import com.tang.kite.sql.ast.SqlNode
import com.tang.kite.sql.dialect.SqlDialect

/**
 * @author Tang
 */
interface DqlHandler<T> {

    fun handleSelect(selectNode: SqlNode.Select, dialect: SqlDialect): T

}
