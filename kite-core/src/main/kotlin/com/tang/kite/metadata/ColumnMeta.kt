package com.tang.kite.metadata

import java.sql.JDBCType

/**
 * Column metadata
 *
 * @author Tang
 */
class ColumnMeta(

    /**
     * Database catalog
     */
    val catalog: String? = null,

    /**
     *  Database schema
     */
    val schema: String? = null,

    /**
     * Table name
     */
    val tableName: String? = null,

    /**
     * Column name
     */
    val columnName: String,

    /**
     * SQL type from java.sql.Types
     */
    val dataType: Int = JDBCType.NULL.vendorTypeNumber,

    /**
     *  Database-specific type name
     */
    val typeName: String,

    /**
     * Standard JDBC type enum
     */
    val jdbcType: JDBCType = JDBCType.NULL,

    /**
     * Column size, length, or precision
     */
    val columnSize: Int = 0,

    /**
     * Decimal digits scale
     */
    val decimalDigits: Int = 0,

    /**
     * Whether column allows null
     */
    val nullable: Boolean = true,

    /**
     *  Default value for column
     */
    val defaultValue: String? = null,

    /**
     * Ordinal position of the column in the table (starting from 1)
     */
    val ordinalPosition: Int = 0,

    /**
     * Comment or description for the column
     */
    val comment: String? = null,

    /**
     *  Whether this column is part of the primary key
     */
    val primaryKey: Boolean = false,

    /**
     * Whether this column has a unique constraint
     */
    val unique: Boolean = false,

    /**
     * Whether this column is auto-increment
     */
    val autoIncrement: Boolean = false,

    /**
     * Whether this is a generated/computed column
     */
    val generatedColumn: Boolean = false

)
