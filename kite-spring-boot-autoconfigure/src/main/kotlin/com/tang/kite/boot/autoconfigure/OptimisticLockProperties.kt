package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.optimistic.OptimisticLockConfig
import com.tang.kite.config.optimistic.OptimisticLockProcessor
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Optimistic lock properties for optimistic lock configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = OptimisticLockProperties.OPTIMISTIC_LOCK_PREFIX)
open class OptimisticLockProperties(

    /**
     * Whether optimistic locking is enabled
     */
    var enabled: Boolean = OptimisticLockConfig.enabled,

    /**
     * The name of the version field
     */
    var fieldName: String = OptimisticLockConfig.fieldName,

    /**
     * Default initial version value
     */
    var initialVersion: Long = OptimisticLockConfig.initialVersion,

    /**
     * Whether to throw exception when optimistic lock fails
     */
    var throwOnFailure: Boolean = OptimisticLockConfig.throwOnFailure,

    /**
     * Optimistic lock processor for handling version operations
     */
    var optimisticLockProcessor: OptimisticLockProcessor = OptimisticLockConfig.optimisticLockProcessor

) : PropertyApplier {

    companion object {

        const val OPTIMISTIC_LOCK_PREFIX = KiteProperties.KITE_PREFIX + ".optimistic-lock"

    }

    override fun applyProperties() {
        OptimisticLockConfig.enabled = enabled
        OptimisticLockConfig.fieldName = fieldName
        OptimisticLockConfig.initialVersion = initialVersion
        OptimisticLockConfig.throwOnFailure = throwOnFailure
        OptimisticLockConfig.optimisticLockProcessor = optimisticLockProcessor
    }

}
