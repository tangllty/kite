package com.tang.jkorm.annotation.id

import com.tang.jkorm.annotation.id.strategy.snowflake.SnowflakeIdStrategy
import com.tang.jkorm.annotation.id.strategy.IdStrategy
import kotlin.reflect.KClass

/**
 * ID annotation
 *
 * @author Tang
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Id(

    /**
     * Id type, default is [IdType.NONE]
     */
    val type: IdType = IdType.NONE,

    /**
     * Autofill id field when the [type] is [IdType.GENERATOR], default is [SnowflakeIdStrategy].
     * If you want to use your own id strategy, you can implement [IdStrategy] and set it here.
     */
    val idStrategy: KClass<out IdStrategy> = SnowflakeIdStrategy::class

)
