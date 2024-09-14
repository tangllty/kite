package com.tang.jkorm.sql.defaults

import com.tang.jkorm.sql.SqlProviderFactory
import com.tang.jkorm.sql.provider.SqlProvider
import com.tang.jkorm.sql.provider.derby.DerbySqlProvider
import com.tang.jkorm.sql.provider.mysql.MysqlSqlProvider
import com.tang.jkorm.sql.provider.postgresql.PostgresqlSqlProvider

/**
 * Default SQL provider factory
 *
 * @author Tang
 */
class DefaultSqlProviderFactory : SqlProviderFactory {

    override fun newSqlProvider(url: String): SqlProvider {
        val urlProviderMap = mapOf(
            "postgresql" to PostgresqlSqlProvider(),
            "mysql" to MysqlSqlProvider(),
            "derby" to DerbySqlProvider()
        )
        val provider = urlProviderMap.entries.find { url.contains(":${it.key}:") }?.value
        return provider ?: throw IllegalArgumentException("Unsupported database url: $url")
    }

}
