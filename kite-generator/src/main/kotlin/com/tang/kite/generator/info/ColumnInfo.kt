package com.tang.kite.generator.info

/**
 * Column information
 *
 * @author Tang
 */
data class ColumnInfo(

    val columnName: String,

    val propertyName: String,

    val dataType: String,

    val targetType: TargetType,

    val comment: String? = null,

    val isNullable: Boolean = true,

    val isPrimaryKey: Boolean = false,

    val isAutoIncrement: Boolean = false,

    val defaultValue: String? = null

)
