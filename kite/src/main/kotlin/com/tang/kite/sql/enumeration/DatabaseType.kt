package com.tang.kite.sql.enumeration

/**
 * Database type enumeration
 *
 * @author Tang
 */
enum class DatabaseType(val url: String) {

    /**
     * PostgreSQL database
     */
    POSTGRE_SQL("postgresql"),

    /**
     * SQLite database
     */
    SQLITE("sqlite"),

    /**
     * H2 database
     */
    H2("h2"),

    /**
     * Lealone database
     */
    LEALONE("lealone"),

    /**
     * HSQL database
     */
    HSQL("hsql"),

    /**
     * Kingbase database
     */
    KINGBASE_ES("kingbasees"),

    /**
     * Phoenix HBase database
     */
    PHOENIX("phoenix"),

    /**
     * SAP HANA database
     */
    SAP_HANA("hana"),

    /**
     * Impala database
     */
    IMPALA("impala"),

    /**
     * HighGo database
     */
    HIGH_GO("highgo"),

    /**
     * Vertica database
     */
    VERTICA("vertica"),

    /**
     * Amazon Redshift database
     */
    REDSHIFT("redshift"),

    /**
     * Gauss database
     */
    GAUSS("gauss"),

    /**
     * Huawei openGauss database
     */
    OPENGAUSS("opengauss"),

    /**
     * TDengine database
     */
    TDENGINE("TDengine"),

    /**
     * UXDB database
     */
    UXDB("uxdb"),

    /**
     * GBase 8s PostgreSQL compatible database
     */
    GBASE_8S_PG("gbase-8s-pg"),

    /**
     * GBase 8c database
     */
    GBASE_8C("gbase-8c"),

    /**
     * Vastbase database
     */
    VASTBASE("vastbase"),

    /**
     * DuckDB database
     */
    DUCKDB("duckdb"),

    /**
     * MySQL database
     */
    MYSQL("mysql"),

    /**
     * MariaDB database
     */
    MARIADB("mariadb"),

    /**
     * GBase (Huaku) database
     */
    GBASE("gbase"),

    /**
     * Oscar database
     */
    OSCAR("oscar"),

    /**
     * Xugu database
     */
    XUGU("xugu"),

    /**
     * ClickHouse database
     */
    CLICK_HOUSE("clickhouse"),

    /**
     * OceanBase database
     */
    OCEAN_BASE("oceanbase"),

    /**
     * CUBRID database
     */
    CUBRID("cubrid"),

    /**
     * SUNDB database
     */
    SUNDB("sundb"),

    /**
     * GoldenDB database
     */
    GOLDENDB("goldendb"),

    /**
     * YASDB database
     */
    YASDB("yasdb"),

    /**
     * Dameng database
     */
    DM("dm"),

    /**
     * Oracle 11g and below database
     */
    ORACLE("oracle"),

    /**
     * CSIIDB database
     */
    CSIIDB("csiidb"),

    /**
     * DB2 database
     */
    DB2("db2"),

    /**
     * DB2 version 10.5 database
     */
    DB2_1005("db2_1005"),

    /**
     * Derby database
     */
    DERBY("derby"),

    /**
     * Doris database
     */
    DORIS("doris"),

    /**
     * Firebird database
     */
    FIREBIRD("Firebird"),

    /**
     * GBase 8s database
     */
    GBASE_8S("gbase-8s"),

    /**
     * GOLDILOCKS database
     */
    GOLDILOCKS("goldilocks"),

    /**
     * Greenplum database
     */
    GREENPLUM("greenplum"),

    /**
     * Hive SQL database
     */
    HIVE("Hive"),

    /**
     * Informix database
     */
    INFORMIX("informix"),

    /**
     * Oracle 12c and above database
     */
    ORACLE_12C("oracle12c"),

    /**
     * Presto database
     */
    PRESTO("presto"),

    /**
     * SinoDB database
     */
    SINODB("sinodb"),

    /**
     * SQL Server database
     */
    SQLSERVER("sqlserver"),

    /**
     * SQL Server 2005 database
     */
    SQLSERVER_2005("sqlserver_2005"),

    /**
     * Sybase ASE database
     */
    SYBASE("sybase"),

    /**
     * Trino database
     */
    TRINO("trino"),

    /**
     * XCloud database
     */
    X_CLOUD("xcloud");

}
