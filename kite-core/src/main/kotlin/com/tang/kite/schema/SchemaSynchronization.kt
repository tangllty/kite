package com.tang.kite.schema

import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger

/**
 * Schema initializer for automatic database schema management based on entity classes.
 *
 * @author Tang
 */
class SchemaSynchronization(private val databaseValue: DatabaseValue) {

    private val logger = getLogger

    private val tableSynchronization = TableSynchronization(databaseValue)

    /**
     * Synchronize schema by scanning packages and synchronizing table structure
     */
    fun synchronizeSchema() {
        logger.info("Synchronizing database schema for database '${databaseValue.sqlDialect.getType().name}'")
        val entityClasses = SchemaScanner.scanEntityClasses(SchemaConfig.scanPackages)
        logger.info("Found ${entityClasses.size} entity classes")
        if (entityClasses.isEmpty()) {
            logger.warn("No entity classes found, schema initialization skipped")
            return
        }
        tableSynchronization.synchronizeTables(entityClasses)
        logger.info("Schema synchronization completed")
    }

    companion object {

        @JvmStatic
        fun <T> getMissing(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
            val existingKeys = existing.map { getKey(it).lowercase() }
            return expected.filterNot { getKey(it).lowercase() in existingKeys }
        }

        @JvmStatic
        fun <T> getExtra(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
            val expectedKeys = expected.map { getKey(it).lowercase() }
            return existing.filterNot { getKey(it).lowercase() in expectedKeys }
        }

    }

}
