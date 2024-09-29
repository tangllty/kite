package com.tang.jkorm.config

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

    companion object {

        val INSTANCE = JkOrmConfig()

    }

}
