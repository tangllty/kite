package com.tang.kite.handler.fill

import com.tang.kite.enumeration.SqlType

/**
 * @author Tang
 */
data class Fill(

    val annotation: Annotation,

    val handler: FillHandler,

    val sqlType: SqlType

)
