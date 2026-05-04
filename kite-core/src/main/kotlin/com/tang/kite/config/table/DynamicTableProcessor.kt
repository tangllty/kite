package com.tang.kite.config.table

import com.tang.kite.config.TableProcessor

/**
 * Dynamic table processor interface
 *
 * @author Tang
 */
interface DynamicTableProcessor : TableProcessor {

    /**
     * Process table name
     *
     * @param tableName table name
     * @return processed table name
     */
    fun process(tableName: String): String

}
