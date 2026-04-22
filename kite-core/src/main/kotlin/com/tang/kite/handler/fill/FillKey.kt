package com.tang.kite.handler.fill

import com.tang.kite.enumeration.SqlType
import kotlin.reflect.KClass

/**
 * @author Tang
 */
data class FillKey(

    val annotationClass: KClass<out Annotation>,

    val sqlType: SqlType

) {

    constructor(annotationClass:Class<out Annotation>, sqlType: SqlType) : this(annotationClass.kotlin, sqlType)

}
