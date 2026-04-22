package com.tang.kite.sql.factory

import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * @author Tang
 */
interface SqlDialectFactory {

    fun createSqlDialect(url: String): SqlDialect

    fun createSqlDialect(databaseType: DatabaseType): SqlDialect

}
