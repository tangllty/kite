package com.tang.kite.config.tenant

import java.lang.reflect.Field

/**
 * Core interface for multi-tenant processor
 *
 * @author Tang
 */
interface TenantProcessor {

    /**
     * Determine whether a specific table entity class requires multi-tenant processing.
     *
     * @param tableClass The table class to check
     * @return true if the table needs processing, false otherwise
     */
    fun isTableNeedProcessing(tableClass: Class<*>): Boolean {
        return true
    }

    /**
     * Get the list of tenant IDs in the current context
     *
     * Rules:
     * - CREATE/UPDATE/DELETE: Use the first ID in the list
     * - QUERY: Use all IDs in the list
     *
     * @param field The tenant field
     * @return List of tenant IDs
     */
    fun getTenantIds(field: Field): List<Any>

    /**
     * Get the first tenant ID in the current context
     *
     * @param field The tenant field
     * @return The first tenant ID in the current context, or null if not found
     */
    fun getFirstTenantId(field: Field): Any? {
        return getTenantIds(field).firstOrNull()
    }

}
