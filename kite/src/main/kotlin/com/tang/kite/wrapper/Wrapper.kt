package com.tang.kite.wrapper

import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.statement.SqlStatement

/**
 * Marker interface for all wrappers
 *
 * @author Tang
 */
interface Wrapper<T> {

    fun setTableClassIfNotSet(clazz: Class<T>)

    fun setTableFillFields()

    fun getSqlStatement(dialect: SqlDialect? = null): SqlStatement

}
