package com.tang.kite.datasource

import com.tang.kite.sql.dialect.SqlDialect
import com.tang.kite.sql.enumeration.DatabaseType
import javax.sql.DataSource

/**
 * @author Tang
 */
data class DatabaseValue(

    val dataSource: DataSource,

    val databaseType: DatabaseType,

    val sqlDialect: SqlDialect

)
