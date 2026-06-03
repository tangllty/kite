package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.DataSourceConfig
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Datasource properties for datasource configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = DataSourceConfigProperties.DATA_SOURCE_CONFIG_PREFIX)
open class DataSourceConfigProperties (

    /**
     * Whether to override the existing data source when registering a new one.
     */
    var override: Boolean = DataSourceConfig.override

) : PropertyApplier {

    companion object {

        const val DATA_SOURCE_CONFIG_PREFIX = KiteProperties.KITE_PREFIX + ".datasource-config"

    }

    override fun applyProperties() {
        DataSourceConfig.override = override
    }

}
