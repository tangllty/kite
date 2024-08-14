package com.tang.jkorm.sql.defaults

import com.tang.jkorm.sql.SqlProviderFactory
import com.tang.jkorm.sql.provider.SqlProvider
import com.tang.jkorm.sql.provider.derby.DerbySqlProvider
import com.tang.jkorm.sql.provider.mysql.MysqlSqlProvider

/**
 * Default SQL provider factory
 *
 * @author Tang
 */
class DefaultSqlProviderFactory : SqlProviderFactory {

    override fun newSqlProvider(driverClass: String): SqlProvider {
        return when (driverClass) {
            "com.mysql.cj.jdbc.Driver" -> MysqlSqlProvider()
            "org.apache.derby.jdbc.EmbeddedDriver" -> DerbySqlProvider()
            else -> throw IllegalArgumentException("Unsupported driver class: $driverClass")
        }
    }

}
