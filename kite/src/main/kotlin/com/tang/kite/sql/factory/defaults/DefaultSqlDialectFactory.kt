package com.tang.kite.sql.factory.defaults

import com.tang.kite.config.KiteConfig
import com.tang.kite.sql.dialect.DerbyDialect
import com.tang.kite.sql.dialect.MysqlDialect
import com.tang.kite.sql.dialect.PostgresqlDialect
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.factory.SqlDialectFactory
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * Default SQL dialect factory
 *
 * @author Tang
 */
class DefaultSqlDialectFactory : SqlDialectFactory {

    override fun newSqlDialect(url: String): SqlDialect {
        val dialects = KiteConfig.dialects
        val databaseType = dialects.keys.firstOrNull { url.contains(":${it.url}:") }
        val dialect = dialects[databaseType]
        return dialect ?: throw IllegalArgumentException("Unsupported database url: $url")
    }

    override fun getDialects(): MutableMap<DatabaseType, SqlDialect> {
        val dialects = mutableMapOf(
            DatabaseType.POSTGRESQL to PostgresqlDialect(),
            DatabaseType.SQLITE to PostgresqlDialect(),
            DatabaseType.H2 to PostgresqlDialect(),

            DatabaseType.MYSQL to MysqlDialect(),

            DatabaseType.DERBY to DerbyDialect(),
        )
        return dialects
    }

}
