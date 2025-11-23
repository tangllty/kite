package com.tang.kite.sql.factory.defaults

import com.tang.kite.config.KiteConfig
import com.tang.kite.sql.dialect.Db2Dialect
import com.tang.kite.sql.dialect.DerbyDialect
import com.tang.kite.sql.dialect.MysqlDialect
import com.tang.kite.sql.dialect.OracleDialect
import com.tang.kite.sql.dialect.PostgresqlDialect
import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.dialect.SqlServerDialect
import com.tang.kite.sql.factory.SqlDialectFactory
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * Default SQL dialect factory
 *
 * @author Tang
 */
class DefaultSqlDialectFactory : SqlDialectFactory {

    override fun newSqlDialect(url: String): SqlDialect {
        val dialects = getDialects()
        val databaseType = dialects.keys.firstOrNull {
            url.contains(":${it.url}:") || url.contains(":${it.url}") || url.contains(it.url)
        }
        return dialects[databaseType] ?: throw IllegalArgumentException("Unsupported database url: $url")
    }

    override fun getDialects(): MutableMap<DatabaseType, SqlDialect> {
        val postgresqlDialect = PostgresqlDialect()
        val mysqlDialect = MysqlDialect()
        val sqlServerDialect = SqlServerDialect()
        val oracleDialect = OracleDialect()
        val db2Dialect = Db2Dialect()

        return mutableMapOf(
            DatabaseType.POSTGRE_SQL to postgresqlDialect,
            DatabaseType.SQLITE to postgresqlDialect,
            DatabaseType.H2 to postgresqlDialect,
            DatabaseType.HSQL to postgresqlDialect,
            DatabaseType.KINGBASE_ES to postgresqlDialect,
            DatabaseType.PHOENIX to postgresqlDialect,
            DatabaseType.SAP_HANA to postgresqlDialect,
            DatabaseType.IMPALA to postgresqlDialect,
            DatabaseType.GAUSS to postgresqlDialect,
            DatabaseType.HIGH_GO to postgresqlDialect,
            DatabaseType.VERTICA to postgresqlDialect,
            DatabaseType.REDSHIFT to postgresqlDialect,
            DatabaseType.OPENGAUSS to postgresqlDialect,
            DatabaseType.TDENGINE to postgresqlDialect,
            DatabaseType.UXDB to postgresqlDialect,
            DatabaseType.LEALONE to postgresqlDialect,
            DatabaseType.DUCKDB to postgresqlDialect,
            DatabaseType.GBASE_8C to postgresqlDialect,
            DatabaseType.GBASE_8S_PG to postgresqlDialect,
            DatabaseType.VASTBASE to postgresqlDialect,
            DatabaseType.TRINO to postgresqlDialect,
            DatabaseType.PRESTO to postgresqlDialect,
            DatabaseType.GREENPLUM to postgresqlDialect,

            DatabaseType.MYSQL to mysqlDialect,
            DatabaseType.MARIADB to mysqlDialect,
            DatabaseType.GBASE to mysqlDialect,
            DatabaseType.OSCAR to mysqlDialect,
            DatabaseType.XUGU to mysqlDialect,
            DatabaseType.CLICK_HOUSE to mysqlDialect,
            DatabaseType.OCEAN_BASE to mysqlDialect,
            DatabaseType.CUBRID to mysqlDialect,
            DatabaseType.SUNDB to mysqlDialect,
            DatabaseType.GOLDENDB to mysqlDialect,
            DatabaseType.YASDB to mysqlDialect,
            DatabaseType.DM to mysqlDialect,
            DatabaseType.DORIS to mysqlDialect,

            DatabaseType.ORACLE to oracleDialect,
            DatabaseType.ORACLE_12C to oracleDialect,
            DatabaseType.CSIIDB to oracleDialect,

            DatabaseType.SQLSERVER to sqlServerDialect,
            DatabaseType.SQLSERVER_2005 to sqlServerDialect,
            DatabaseType.DERBY to sqlServerDialect,
            DatabaseType.SYBASE to sqlServerDialect,

            DatabaseType.DB2 to db2Dialect,
            DatabaseType.DB2_1005 to db2Dialect,


            DatabaseType.FIREBIRD to sqlServerDialect,
            DatabaseType.GBASE_8S to sqlServerDialect,
            DatabaseType.GOLDILOCKS to postgresqlDialect,
            DatabaseType.HIVE to postgresqlDialect,
            DatabaseType.INFORMIX to sqlServerDialect,
            DatabaseType.SINODB to mysqlDialect,
            DatabaseType.X_CLOUD to postgresqlDialect
        )
    }

}
