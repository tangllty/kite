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
     * Table name
     */
    val value: String = "",

    /**
     * Table alias
     */
    val alias: String = "",

    /**
     * Dynamic table name processor
     */
    val dynamicTableName: KClass<out DynamicTableProcessor> = DynamicTableProcessor::class,

    /**
     * Table comment/description
     */
    val comment: String = "",

    /**
     * Table schema/catalog name
     */
    val schema: String = "",

    /**
     * Table character set
     */
    val charset: String = "",

    /**
     * Table collation
     */
    val collation: String = ""

)
