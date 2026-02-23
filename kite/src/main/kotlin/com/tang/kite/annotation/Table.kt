package com.tang.kite.annotation

import com.tang.kite.config.table.DynamicTableProcessor
import kotlin.reflect.KClass

/**
 * Table annotation
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
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
    val dynamicTableName: KClass<out DynamicTableProcessor> = DynamicTableProcessor::class

)
