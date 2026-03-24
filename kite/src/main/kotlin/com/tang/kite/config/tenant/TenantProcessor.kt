package com.tang.kite.config.tenant

import java.lang.reflect.Field

/**
 * Core interface for multi-tenant processor
 *
 * @author Tang
 */
interface TenantProcessor {

    /**
     * Get the set of table entity classes that need multi-tenant filtering.
     * If this set is empty, all table entity classes will be processed.
     *
     * @return A set of Class objects representing the table entity classes to include
     */
    fun getIncludedTableClasses(): Set<Class<*>> = emptySet()

    /**
     * Get the set of table entity classes that need to be excluded from multi-tenant filtering.
     *
     * @return A set of Class objects representing the table entity classes to exclude
     */
    fun getExcludedTableClasses(): Set<Class<*>> = emptySet()

    /**
     * Determine whether a specific table entity class requires multi-tenant processing.
     * The judgment rules are:
     * 1. If the table entity class is in the exclude list, return false (not processed)
     * 2. If the include list is empty, return true (process all except excluded)
     * 3. If the include list is not empty, return true only if the table entity class is in the include list
     *
     * @param tableClass The table class to check
     * @return true if the table needs processing, false otherwise
     */
    fun isTableNeedProcessing(tableClass: Class<*>): Boolean {
        val includedClasses = getIncludedTableClasses()
        val excludedClasses = getExcludedTableClasses()

        // Exclude takes precedence over include: if in exclude list, return false directly
        if (excludedClasses.contains(tableClass)) {
            return false
        }

        // Process if include list is empty (all tables) or the class is in include list
        return includedClasses.isEmpty() || includedClasses.contains(tableClass)
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
