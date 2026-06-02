package com.tang.kite.config

/**
 * Page properties for pagination configuration.
 *
 * @author Tang
 */
object PageConfig {

    /**
     * Default page number.
     */
    @JvmStatic
    var pageNumber = 1L

    /**
     * Default page size.
     */
    @JvmStatic
    var pageSize = 10L

    /**
     * Max page size
     */
    @JvmStatic
    var maxPageSize = 100L

}
