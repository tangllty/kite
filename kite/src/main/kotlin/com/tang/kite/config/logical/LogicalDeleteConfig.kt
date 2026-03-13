package com.tang.kite.config.logical

import com.tang.kite.config.defaults.DefaultLogicalDeleteProcessor

/**
 * Logical delete properties for logical delete configuration.
 *
 * @author Tang
 */
object LogicalDeleteConfig {

    /**
     * Whether logical delete is enabled
     */
    var enabled: Boolean = false

    /**
     * Logical delete field name
     */
    var fieldName: String = "deleted"

    /**
     * Static processor for logical delete operations
     */
    @JvmStatic
    var logicalDeleteProcessor: LogicalDeleteProcessor = DefaultLogicalDeleteProcessor

}
