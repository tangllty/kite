package com.tang.kite.sql.dialect

import com.tang.kite.sql.enumeration.DatabaseType

/**
 * SQL Server SQL provider
 *
 * @author Tang
 */
class SqlServerDialect : AbstractSqlDialect(DatabaseType.SQLSERVER) {

    override fun getAddColumnKeyword(): String = "add"

}
