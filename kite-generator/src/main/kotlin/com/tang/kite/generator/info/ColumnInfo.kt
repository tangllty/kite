package com.tang.kite.generator.info

import com.tang.kite.metadata.ColumnMeta

/**
 * Column information
 *
 * @author Tang
 */
data class ColumnInfo(

    val columnMeta: ColumnMeta,

    val propertyName: String,

    val targetType: TargetType

)
