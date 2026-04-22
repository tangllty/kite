package com.tang.kite.config.table

/**
 * Dynamic table processor interface
 *
 * @author Tang
 */
interface DynamicTableProcessor {

    /**
     * Process table name
     *
     * @param tableName table name
     * @return processed table name
     */
    fun process(tableName: String): String

}
