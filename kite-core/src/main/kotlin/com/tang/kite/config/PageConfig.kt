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
     * Default page number parameter name.
     */
    @JvmStatic
    var pageNumberParameter = "pageNumber"

    /**
     * Default page size parameter name.
     */
    @JvmStatic
    var pageSizeParameter = "pageSize"

}
