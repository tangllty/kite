package com.tang.jkorm.config

import com.tang.jkorm.sql.provider.derby.DerbySqlProvider
import com.tang.jkorm.sql.provider.mysql.MysqlSqlProvider
import com.tang.jkorm.sql.provider.postgresql.PostgresqlSqlProvider
import java.util.function.Function

/**
 * JkOrm configuration
 *
 * @author Tang
 */
class JkOrmConfig {

    var banner = true

    var pageNumber = 1L

    var pageSize = 10L

    var pageNumberParameter = "pageNumber"

    var pageSizeParameter = "pageSize"

    var selectiveStrategy: Function<Any?, Boolean> = Function {
        if (it == null) {
            false
        }
        if (it is String) {
            it.isNotEmpty()
        }
        if (it is Int) {
            it != 0
        }
        true
    }

    val urlProviders = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider()
    )

    companion object {

        val INSTANCE = JkOrmConfig()

    }

}
