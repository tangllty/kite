package com.tang.kite.config.logical

import java.lang.reflect.Field

/**
 * Processor interface for handling logical deletion field processing logic
 *
 * @author Tang
 */
interface LogicalDeletionProcessor {

    /**
     * Process the specified database field to generate logical deletion value
     * @param field Target database field for logical deletion
     * @return Generated logical deletion value
     */
    fun process(field: Field): LogicalDeletionValue

}
