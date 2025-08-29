package com.tang.kite.utils.resultset

import java.sql.JDBCType

/**
 * @author Tang
 */
data class MetaData(

    val className: String,

    val jdbcType: JDBCType,

    val columnName: String,

    val columnValue: Any?

)
