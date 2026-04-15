package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.table.DynamicTableProcessor
import com.tang.kite.config.table.TableConfig
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Table properties for table configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = TableProperties.TABLE_PREFIX)
open class TableProperties (

    /**
     * Dynamic table name processor.
     */
    var dynamicTableProcessor: DynamicTableProcessor? = TableConfig.dynamicTableProcessor

) {

    companion object {

        const val TABLE_PREFIX = KiteProperties.KITE_PREFIX + ".table"

    }

}
