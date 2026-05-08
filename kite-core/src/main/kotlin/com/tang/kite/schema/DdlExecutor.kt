package com.tang.kite.schema

import com.tang.kite.datasource.DatabaseValue
import com.tang.kite.logging.getLogger

/**
 * DDL executor for database schema operations
 *
 * @author Tang
 */
class DdlExecutor(private val databaseValue: DatabaseValue) {

    private val logger = getLogger

    /**
     * Execute raw DDL statement
     */
    fun executeDdl(sql: String): Boolean {
        return executeDdlBatch(listOf(sql))
    }

    /**
     * Execute multiple DDL statements with transaction control
     */
    fun executeDdlBatch(sqlList: List<String>): Boolean {
        val validSqlList = sqlList.filter(String::isNotBlank)
        if (validSqlList.isEmpty()) return true

        return runCatching {
            databaseValue.dataSource.connection.use { connection ->
                val originalAutoCommit = connection.autoCommit
                connection.autoCommit = false

                try {
                    connection.createStatement().use { statement ->
                        validSqlList.forEach { sql ->
                            logger.info("Executing DDL: $sql")
                            statement.execute(sql)
                        }
                    }

                    connection.commit()
                    true
                } catch (e: Exception) {
                    connection.rollback()
                    logger.error("DDL execution failed, rolled back", e)
                    throw e
                } finally {
                    connection.autoCommit = originalAutoCommit
                }
            }
        }.getOrDefault(false)
    }

}
