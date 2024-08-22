package com.tang.jkorm.spring.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

/**
 * Mapper component annotation
 *
 * @author Tang
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class Mapper(

    @get:AliasFor(annotation = Component::class)
    val value: String = ""

)
