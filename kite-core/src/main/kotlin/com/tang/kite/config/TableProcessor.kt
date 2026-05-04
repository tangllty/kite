package com.tang.kite.config

/**
 * Interface for checking whether a table needs processing
 *
 * @author Tang
 */
interface TableProcessor {

    /**
     * Determine whether a specific table class needs to be processed.
     *
     * @param tableClass The table class to check
     * @return true if the table needs processing, false otherwise
     */
    fun processable(tableClass: Class<*>): Boolean {
        return true
    }

}
