package com.tang.kite.sql.factory

import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * @author Tang
 */
interface SqlDialectFactory {

    fun newSqlDialect(url: String): SqlDialect

    fun getDialects(): MutableMap<DatabaseType, SqlDialect>

}
