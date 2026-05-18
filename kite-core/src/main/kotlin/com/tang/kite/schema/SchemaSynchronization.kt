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
        val dialect = databaseValue.sqlDialect.getType().name
        logger.info("Starting schema synchronization for '$dialect' database")
        val entityClasses = SchemaScanner.scanEntityClasses(SchemaConfig.scanPackages)
        logger.info("Scanned ${entityClasses.size} entity classes for schema synchronization")
        if (entityClasses.isEmpty()) {
            logger.warn("No entity classes found in scan packages, schema synchronization skipped")
            return
        }
        tableSynchronization.synchronizeTables(entityClasses)
        logger.info("Schema synchronization completed successfully")
    }

    companion object {

        @JvmStatic
        fun <T> getMissing(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
            return expected.filter { expectedItem ->
                existing.none { existingItem ->
                    getKey(existingItem).equals(getKey(expectedItem), ignoreCase = true)
                }
            }
        }

        @JvmStatic
        fun <T> getExtra(existing: List<T>, expected: List<T>, getKey: (T) -> String): List<T> {
            return existing.filter { existingItem ->
                expected.none { expectedItem ->
                    getKey(existingItem).equals(getKey(expectedItem), ignoreCase = true)
                }
            }
        }

    }

}
