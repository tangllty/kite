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
     * Database column name, use field name if empty
     */
    val value: String = "",

    /**
     * Whether to ignore this column when generating SQL
     */
    val ignore: Boolean = false,

    /**
     * Custom result handler for column value conversion
     *
     * @see ResultHandler
     */
    val resultHandler: KClass<out ResultHandler> = ResultHandler::class,

    /**
     * Query condition operator
     *
     * @see ColumnOperator
     */
    val operator: ColumnOperator = ColumnOperator.EQUAL,

    /**
     * Database column data type
     *
     * @see DataType
     */
    val dataType: String = DataType.VARCHAR,

    /**
     * Column length for VARCHAR, CHAR, etc.
     */
    val length: Int = 255,

    /**
     * Numeric precision for DECIMAL, NUMERIC, etc.
     */
    val precision: Int = 10,

    /**
     * Numeric scale for DECIMAL, NUMERIC, etc.
     */
    val scale: Int = 2,

    /**
     * Whether column allows null value
     */
    val nullable: Boolean = true,

    /**
     * Database column default value
     */
    val defaultValue: String = "",

    /**
     * Database column comment
     */
    val comment: String = "",

    /**
     * Whether to create ordinary index for this column
     */
    val index: Boolean = false,

    /**
     * Whether to add unique constraint on this column
     */
    val unique: Boolean = false,

    /**
     * Referenced foreign key table name
     */
    val foreignKey: String = "",

    /**
     * Referenced column name of foreign key table
     */
    val foreignKeyColumn: String = "",

    /**
     * Foreign key action on delete
     *
     * @see ForeignKeyAction
     */
    val onDelete: ForeignKeyAction = ForeignKeyAction.NO_ACTION,

    /**
     * Foreign key action on update
     *
     * @see ForeignKeyAction
     */
    val onUpdate: ForeignKeyAction = ForeignKeyAction.NO_ACTION

)
