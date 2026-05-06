package com.tang.kite.handler.field

import com.tang.kite.enumeration.SqlType

/**
 * @author Tang
 */
data class FieldMeta(

    val annotation: Annotation,

    val handler: FieldHandler,

    val sqlType: SqlType

)
