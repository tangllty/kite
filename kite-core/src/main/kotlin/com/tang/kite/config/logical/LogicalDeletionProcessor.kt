package com.tang.kite.config.logical

import java.lang.reflect.Field

/**
 * Processor interface for handling logical deletion field processing logic
 *
 * @author Tang
 */
interface LogicalDeletionProcessor {

    /**
     * Determine whether a specific table class needs to be processed.
     *
     * @param tableClass The table class to check
     * @return true if the table needs processing, false otherwise
     */
    fun isTableNeedProcessing(tableClass: Class<*>): Boolean {
        return true
    }

    /**
     * Process the specified database field to generate logical deletion value
     *
     * @param field Target database field for logical deletion
     * @return Generated logical deletion value
     */
    fun process(field: Field): LogicalDeletionValue

}
