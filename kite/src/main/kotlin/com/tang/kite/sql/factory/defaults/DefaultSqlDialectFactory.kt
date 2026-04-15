package com.tang.kite.sql.factory.defaults

import com.tang.kite.sql.dialect.Db2Dialect
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
object DefaultSqlDialectFactory : SqlDialectFactory {

    override fun createSqlDialect(url: String): SqlDialect {
        val databaseType = DatabaseType.getDatabaseType(url)
        return createSqlDialect(databaseType)
    }

    override fun createSqlDialect(databaseType: DatabaseType): SqlDialect {
        return when (databaseType) {
            DatabaseType.POSTGRE_SQL,
            DatabaseType.SQLITE,
            DatabaseType.H2,
            DatabaseType.HSQL,
            DatabaseType.KINGBASE_ES,
            DatabaseType.PHOENIX,
            DatabaseType.SAP_HANA,
            DatabaseType.IMPALA,
            DatabaseType.GAUSS,
            DatabaseType.HIGH_GO,
            DatabaseType.VERTICA,
            DatabaseType.REDSHIFT,
            DatabaseType.OPENGAUSS,
            DatabaseType.TDENGINE,
            DatabaseType.UXDB,
            DatabaseType.LEALONE,
            DatabaseType.DUCKDB,
            DatabaseType.GBASE_8C,
            DatabaseType.GBASE_8S_PG,
            DatabaseType.VASTBASE,
            DatabaseType.TRINO,
            DatabaseType.PRESTO,
            DatabaseType.GREENPLUM,
            DatabaseType.GOLDILOCKS,
            DatabaseType.HIVE,
            DatabaseType.X_CLOUD -> PostgresqlDialect()

            DatabaseType.MYSQL,
            DatabaseType.MARIADB,
            DatabaseType.GBASE,
            DatabaseType.OSCAR,
            DatabaseType.XUGU,
            DatabaseType.CLICK_HOUSE,
            DatabaseType.OCEAN_BASE,
            DatabaseType.CUBRID,
            DatabaseType.SUNDB,
            DatabaseType.GOLDENDB,
            DatabaseType.YASDB,
            DatabaseType.DM,
            DatabaseType.DORIS,
            DatabaseType.SINODB -> MysqlDialect()

            DatabaseType.ORACLE,
            DatabaseType.ORACLE_12C,
            DatabaseType.CSIIDB -> OracleDialect()

            DatabaseType.SQLSERVER,
            DatabaseType.SQLSERVER_2005,
            DatabaseType.DERBY,
            DatabaseType.SYBASE,
            DatabaseType.FIREBIRD,
            DatabaseType.GBASE_8S,
            DatabaseType.INFORMIX -> SqlServerDialect()

            DatabaseType.DB2,
            DatabaseType.DB2_1005 -> Db2Dialect()
        }
    }

}
