package com.tang.jkorm.annotation

import com.tang.jkorm.utils.id.IdStrategy
import com.tang.jkorm.utils.id.defaults.DefaultIdStrategy
import kotlin.reflect.KClass

/**
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Id(

    val autoIncrement: Boolean = true,

    val idStrategy: KClass<out IdStrategy> = DefaultIdStrategy::class

)
