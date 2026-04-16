package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.PageConfig
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Page properties for pagination configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = PageProperties.PAGE_PREFIX)
open class PageProperties (

    /**
     * Default page number.
     */
    var pageNumber: Long = PageConfig.pageNumber,

    /**
     * Default page size.
     */
    var pageSize: Long = PageConfig.pageSize,

    /**
     * Default page number parameter name.
     */
    var pageNumberParameter: String = PageConfig.pageNumberParameter,

    /**
     * Default page size parameter name.
     */
    var pageSizeParameter: String = PageConfig.pageSizeParameter

) : PropertyApplier {

    companion object {

        const val PAGE_PREFIX = KiteProperties.KITE_PREFIX + ".page"

    }

    override fun applyProperties() {
        PageConfig.pageNumber = pageNumber
        PageConfig.pageSize = pageSize
        PageConfig.pageNumberParameter = pageNumberParameter
        PageConfig.pageSizeParameter = pageSizeParameter
    }

}
