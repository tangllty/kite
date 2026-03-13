package com.tang.kite.config.logical

import java.lang.reflect.Field

/**
 * Processor interface for handling logical delete field processing logic
 *
 * @author Tang
 */
interface LogicalDeleteProcessor {

    /**
     * Process the specified database field to generate logical delete value
     * @param field Target database field for logical deletion
     * @return Generated logical delete value
     */
    fun process(field: Field): LogicalDeleteValue

}
