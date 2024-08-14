package com.tang.jkorm.session

import com.tang.jkorm.sql.provider.SqlProvider
import javax.sql.DataSource

/**
 * @author Tang
 */
class Configuration(

    val dataSource: DataSource,

    val sqlProvider: SqlProvider

)
