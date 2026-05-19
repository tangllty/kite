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
     * SQL lowercase setting.
     */
    @JvmStatic
    var sqlLowercase = true

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

    /**
     * Whether to create missing indexes
     */
    @JvmStatic
    var createMissingIndexes = true

    /**
     * Whether to modify existing indexes
     */
    @JvmStatic
    var modifyIndexes = false

    /**
     * Whether to drop existing indexes
     */
    @JvmStatic
    var dropExistingIndexes = false

    @JvmStatic
    fun getSql(sql: String): String {
        return if (sqlLowercase) {
            sql.lowercase()
        } else {
            sql.uppercase()
        }
    }

    @JvmStatic
    fun getSqlNullable(sql: String?): String? {
        return sql?.let { getSql(it) }
    }

    @JvmStatic
    fun getSqlCondition(sql: String?): String? {
        if (sql == null) return null
        TODO("parse sql")
    }

}
