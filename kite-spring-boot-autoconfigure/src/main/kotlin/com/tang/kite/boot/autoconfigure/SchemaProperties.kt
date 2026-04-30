package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.schema.SchemaConfig
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Schema properties for schema configuration.
 *
 * @author Tang
 */
@Suppress("ConfigurationProperties")
@ConfigurationProperties(prefix = SchemaProperties.SCHEMA_PREFIX)
open class SchemaProperties(

    /**
     * Whether schema management is enabled
     */
    var enabled: Boolean = SchemaConfig.enabled,

    /**
     * Packages to scan for entity classes
     * Supports wildcard patterns (e.g., "com.example.*", "com.example.**")
     */
    var scanPackages: MutableSet<String> = SchemaConfig.scanPackages,

    /**
     * Whether to create missing tables
     */
    var createMissingTables: Boolean = SchemaConfig.createMissingTables,

    /**
     * Whether to drop existing tables
     */
    var dropExistingTables: Boolean = SchemaConfig.dropExistingTables,

    /**
     * Whether to create missing columns in the database
     */
    var createMissingColumns: Boolean = SchemaConfig.createMissingColumns,

    /**
     * Whether to drop extra columns in the database
     */
    var dropExistingColumns: Boolean = SchemaConfig.dropExistingColumns

) : PropertyApplier {

    companion object {

        const val SCHEMA_PREFIX = KiteProperties.KITE_PREFIX + ".schema"

    }

    override fun applyProperties() {
        SchemaConfig.enabled = enabled
        SchemaConfig.scanPackages = scanPackages
        SchemaConfig.createMissingTables = createMissingTables
        SchemaConfig.dropExistingTables = dropExistingTables
        SchemaConfig.createMissingColumns = createMissingColumns
        SchemaConfig.dropExistingColumns = dropExistingColumns
    }

}
