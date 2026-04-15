package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.logical.LogicalDeletionConfig
import com.tang.kite.config.logical.LogicalDeletionProcessor
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Logical delete properties for logical delete configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = LogicalDeletionProperties.LOGICAL_DELETE_PREFIX)
open class LogicalDeletionProperties (

    /**
     * Whether logical delete is enabled
     */
    val enabled: Boolean = LogicalDeletionConfig.enabled,

    /**
     * Logical delete field name
     */
    val fieldName: String = LogicalDeletionConfig.fieldName,

    /**
     * Static processor for logical delete operations
     */
    var logicalDeletionProcessor: LogicalDeletionProcessor = LogicalDeletionConfig.logicalDeletionProcessor

) {

    companion object {

        const val LOGICAL_DELETE_PREFIX = KiteProperties.KITE_PREFIX + ".logical-delete"

    }

}
