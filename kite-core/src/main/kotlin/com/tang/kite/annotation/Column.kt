package com.tang.kite.annotation

import com.tang.kite.enumeration.ColumnOperator
import com.tang.kite.handler.result.ResultHandler
import com.tang.kite.sql.ast.ForeignKeyAction
import com.tang.kite.sql.datatype.DataType
import kotlin.reflect.KClass

/**
 * Column annotation for mapping class fields to database columns
 *
 * @author Tang
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Column(

    /**
     * Column name
     */
    val value: String = "",

    /**
     * Ignore this column when generate SQL
     */
    val ignore: Boolean = false,

    /**
     * Result handler for this column
     *
     * @see ResultHandler
     */
    val resultHandler: KClass<out ResultHandler> = ResultHandler::class,

    /**
     * Column operator
     *
     * @see ColumnOperator
     */
    val operator: ColumnOperator = ColumnOperator.EQUAL,

    /**
     * Column data type
     *
     * @see DataType
     */
    val dataType: String = DataType.VARCHAR,

    /**
     * Column length for VARCHAR, CHAR, etc.
     */
    val length: Int = 255,

    /**
     * Column precision for DECIMAL, NUMERIC, etc.
     */
    val precision: Int = 10,

    /**
     * Column scale for DECIMAL, NUMERIC, etc.
     */
    val scale: Int = 2,

    /**
     * Whether the column is nullable
     */
    val nullable: Boolean = true,

    /**
     * Default value for the column
     */
    val defaultValue: String = "",

    /**
     * Whether the column is auto-increment
     */
    @Deprecated("")
    val autoIncrement: Boolean = false,

    /**
     * Whether the column is primary key
     */
    @Deprecated("")
    val primaryKey: Boolean = false,

    /**
     * Whether the column has unique constraint
     */
    val unique: Boolean = false,

    /**
     * Column comment/description
     */
    val comment: String = "",

    /**
     * Whether this column should have an index
     */
    val indexed: Boolean = false,

    /**
     * Index name for this column (empty means generated name)
     */
    val indexName: String = "",

    /**
     * Whether the index is unique
     */
    val uniqueIndex: Boolean = false,

    /**
     * Foreign key table reference
     */
    val foreignKey: String = "",

    /**
     * Foreign key column reference
     */
    val foreignKeyColumn: String = "",

    /**
     * ON DELETE action for foreign key
     */
    val onDelete: ForeignKeyAction = ForeignKeyAction.NO_ACTION,

    /**
     * ON UPDATE action for foreign key
     */
    val onUpdate: ForeignKeyAction = ForeignKeyAction.NO_ACTION

)
