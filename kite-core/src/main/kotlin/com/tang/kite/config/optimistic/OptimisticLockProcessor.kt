package com.tang.kite.config.optimistic

import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.metadata.ColumnMeta
import com.tang.kite.metadata.TableMeta
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * Processor for optimistic locking operations
 *
 * Handles version field detection, initialization, and increment operations
 *
 * @author Tang
 */
interface OptimisticLockProcessor {

    /**
     * Determine whether a specific table entity class requires optimistic lock processing.
     *
     * @param tableClass The table class to check
     * @return true if the table needs processing, false otherwise
     */
    fun isTableNeedProcessing(tableClass: Class<*>): Boolean {
        return true
    }

    /**
     * Check if an entity has a version field
     */
    fun hasVersionField(entity: Any): Boolean

    /**
     * Get the version field from an entity
     */
    fun getVersionField(entity: Any): Field?

    /**
     * Get the current version value from an entity
     */
    fun getVersionValue(entity: Any): Long?

    /**
     * Initialize version field with initial value
     */
    fun initializeVersion(entity: Any)

    /**
     * Increment version field value
     */
    fun incrementVersion(entity: Any)

    /**
     * Check if version field is supported type
     */
    fun isSupportedVersionType(field: Field): Boolean

}
