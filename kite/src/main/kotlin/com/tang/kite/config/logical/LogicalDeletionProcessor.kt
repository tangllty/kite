package com.tang.kite.config.logical

import java.lang.reflect.Field

/**
 * Processor interface for handling logical deletion field processing logic
 *
 * @author Tang
 */
interface LogicalDeletionProcessor {

    /**
     * Get the set of table classes that need to be included for processing.
     * If this set is empty, all table classes will be processed.
     *
     * @return A set of Class objects representing the tables to include
     */
    fun getIncludedTableClasses(): Set<Class<*>> = emptySet()

    /**
     * Get the set of table classes that need to be excluded from processing.
     *
     * @return A set of Class objects representing the tables to exclude
     */
    fun getExcludedTableClasses(): Set<Class<*>> = emptySet()

    /**
     * Determine whether a specific table class needs to be processed.
     * The judgment rules are:
     * 1. If the table class is in the exclude list, return false (not processed)
     * 2. If the include list is empty, return true (process all except excluded)
     * 3. If the include list is not empty, return true only if the table class is in the include list
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
     * Process the specified database field to generate logical deletion value
     *
     * @param field Target database field for logical deletion
     * @return Generated logical deletion value
     */
    fun process(field: Field): LogicalDeletionValue

}
