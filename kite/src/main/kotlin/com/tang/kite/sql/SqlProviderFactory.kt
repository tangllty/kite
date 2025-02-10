package com.tang.kite.sql

import com.tang.kite.sql.provider.SqlProvider

/**
 * SQL provider factory
 *
 * @author Tang
 */
interface SqlProviderFactory {

    fun newSqlProvider(url: String): SqlProvider

}
