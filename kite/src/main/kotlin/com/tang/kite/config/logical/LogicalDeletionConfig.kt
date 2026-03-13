package com.tang.kite.config.logical

import com.tang.kite.config.defaults.DefaultLogicalDeletionProcessor

/**
 * Logical deletion properties for logical deletion configuration.
 *
 * @author Tang
 */
object LogicalDeletionConfig {

    /**
     * Whether logical deletion is enabled
     */
    var enabled: Boolean = false

    /**
     * Logical deletion field name
     */
    var fieldName: String = "deleted"

    /**
     * Static processor for logical deletion operations
     */
    @JvmStatic
    var logicalDeletionProcessor: LogicalDeletionProcessor = DefaultLogicalDeletionProcessor

}
