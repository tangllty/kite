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
     * Max page size.
     */
    var maxPageSize: Long = PageConfig.maxPageSize

) : PropertyApplier {

    companion object {

        const val PAGE_PREFIX = KiteProperties.KITE_PREFIX + ".page"

    }

    override fun applyProperties() {
        PageConfig.pageNumber = pageNumber
        PageConfig.pageSize = pageSize
        PageConfig.maxPageSize = maxPageSize
    }

}
