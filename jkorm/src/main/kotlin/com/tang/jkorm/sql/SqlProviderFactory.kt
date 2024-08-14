package com.tang.jkorm.sql

import com.tang.jkorm.sql.provider.SqlProvider

/**
 * SQL provider factory
 *
 * @author Tang
 */
interface SqlProviderFactory {

    fun newSqlProvider(driverClass: String): SqlProvider

}
