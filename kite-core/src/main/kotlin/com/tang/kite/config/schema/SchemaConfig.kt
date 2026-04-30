package com.tang.kite.config.schema

/**
 * Schema Configuration for database schema management
 *
 * @author Tang
 */
object SchemaConfig {

    /**
     * Whether schema management is enabled
     */
    @JvmStatic
    var enabled: Boolean = false

    /**
     * Packages to scan for entity classes
     * Supports wildcard patterns (e.g., "com.example.*", "com.example.**")
     */
    @JvmStatic
    var scanPackages: MutableSet<String> = mutableSetOf()

    /**
     * Whether to create missing tables
     */
    @JvmStatic
    var createMissingTables = true

    /**
     * Whether to drop existing tables
     */
    @JvmStatic
    var dropExistingTables = false

    /**
     * Whether to create missing columns in the database
     */
    @JvmStatic
    var createMissingColumns = true

    /**
     * Whether to drop extra columns in the database
     */
    @JvmStatic
    var dropExistingColumns = false

}
