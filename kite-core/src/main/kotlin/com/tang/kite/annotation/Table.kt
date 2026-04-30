package com.tang.kite.annotation

import com.tang.kite.config.table.DynamicTableProcessor
import kotlin.reflect.KClass

/**
 * Table annotation for mapping classes to database tables
 *
 * @author Tang
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Table(

    /**
     * Database table name, uses class name if empty
     */
    val value: String = "",

    /**
     * SQL query alias for the table, uses class name generated if empty
     */
    val alias: String = "",

    /**
     * Processor for dynamic table name generation
     *
     * @see DynamicTableProcessor
     */
    val dynamicTableName: KClass<out DynamicTableProcessor> = DynamicTableProcessor::class,

    /**
     * Table comment or description
     */
    val comment: String = "",

    /**
     * Database schema or catalog where the table resides
     */
    val schema: String = "",

    /**
     * Table character set
     */
    val charset: String = "",

    /**
     * Table collation rules
     */
    val collation: String = ""

)
