package com.tang.kite.annotation

import com.tang.kite.result.ResultHandler
import kotlin.reflect.KClass

/**
 * Column annotation
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
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
    val resultHandler: KClass<out ResultHandler> = ResultHandler::class

)
