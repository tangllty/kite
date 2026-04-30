package com.tang.kite.config.optimistic

import com.tang.kite.config.optimistic.defaults.DefaultOptimisticLockProcessor

/**
 * Optimistic lock configuration properties for database optimistic locking.
 *
 * @author Tang
 */
object OptimisticLockConfig {

    /**
     * Whether optimistic locking is enabled
     */
    @JvmStatic
    var enabled: Boolean = false

    /**
     * The name of the version field
     */
    @JvmStatic
    var versionFieldName: String = "version"

    /**
     * Default initial version value
     */
    @JvmStatic
    var initialVersion: Long = 0L

    /**
     * Whether to throw exception when optimistic lock fails
     */
    @JvmStatic
    var throwOnFailure: Boolean = true

    /**
     * Optimistic lock processor for handling version operations
     */
    @JvmStatic
    var optimisticLockProcessor: OptimisticLockProcessor = DefaultOptimisticLockProcessor()

}
