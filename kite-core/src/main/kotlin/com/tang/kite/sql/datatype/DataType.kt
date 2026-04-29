package com.tang.kite.sql.datatype

/**
 * SQL data type enumeration for DDL operations
 * Provides common data types that can be mapped to specific database dialects
 *
 * @author Tang
 */
object DataType {

    // Character and String Types
    const val CHAR = "char"
    const val VARCHAR = "varchar"
    const val NCHAR = "nchar"
    const val NVARCHAR = "nvarchar"
    const val CLOB = "clob"
    const val NCLOB = "nclob"
    const val TEXT = "text"

    // Numeric Types
    const val TINYINT = "tinyint"
    const val SMALLINT = "smallint"
    const val INT = "int"
    const val BIGINT = "bigint"
    const val REAL = "real"
    const val FLOAT = "float"
    const val DOUBLE_PRECISION = "double precision"
    const val DECIMAL = "decimal"
    const val NUMERIC = "numeric"

    // Date and Time Types
    const val DATE = "date"
    const val TIME = "time"
    const val TIME_WITH_TIME_ZONE = "time with time zone"
    const val TIMESTAMP = "timestamp"
    const val TIMESTAMP_WITH_TIME_ZONE = "timestamp with time zone"
    const val INTERVAL = "interval"

    // Boolean Type
    const val BOOLEAN = "boolean"

    // Binary Data Types
    const val BLOB = "blob"
    const val BINARY = "binary"
    const val VARBINARY = "varbinary"
    const val BIT = "bit"

    // Other Advanced Data Types
    const val XML = "xml"
    const val JSON = "json"
    const val UUID = "uuid"
    const val ARRAY = "array"
    const val MULTISET = "multiset"
    const val ROW = "row"
    const val ENUM = "enum"

    // MySQL
    const val DATETIME = "datetime"
    const val MEDIUMINT = "mediumint"
    const val TINYTEXT = "tinytext"
    const val MEDIUMTEXT = "mediumtext"
    const val LONGTEXT = "longtext"
    const val MEDIUMBLOB = "mediumblob"
    const val LONGBLOB = "longblob"

    // PostgreSQL
    const val JSONB = "jsonb"
    const val BYTEA = "bytea"
    const val SERIAL = "serial"
    const val BIGSERIAL = "bigserial"

    // SQL Server
    const val DATETIME2 = "datetime2"
    const val MONEY = "money"
    const val VARCHAR_MAX = "varchar(max)"
    const val NVARCHAR_MAX = "nvarchar(max)"

    // Oracle
    const val VARCHAR2 = "varchar2"
    const val NVARCHAR2 = "nvarchar2"
    const val NUMBER = "number"

}
