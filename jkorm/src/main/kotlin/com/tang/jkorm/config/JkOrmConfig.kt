package com.tang.jkorm.config

import com.tang.jkorm.config.defaults.DefaultSelectiveStrategy
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

    var selectiveStrategy = (Function<Any?, Boolean> { DefaultSelectiveStrategy.isSelective(it) })

    var sqlLowercase = true

    val urlProviders = mapOf(
        "postgresql" to PostgresqlSqlProvider(),
        "mysql" to MysqlSqlProvider(),
        "derby" to DerbySqlProvider()
    )

    fun getSql(sql: StringBuilder): String {
        return getSql(sql.toString())
    }

    fun getSql(sql: String): String {
        return if (sqlLowercase) {
            sql.lowercase()
        } else {
            sql.uppercase()
        }
    }

    companion object {

        val INSTANCE = JkOrmConfig()

    }

}
