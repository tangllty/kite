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

        val connection = databaseValue.dataSource.connection

        return runCatching {
            connection.use { conn ->
                val originalAutoCommit = conn.autoCommit
                conn.autoCommit = false

                try {
                    conn.createStatement().use { statement ->
                        validSqlList.forEach { sql ->
                            logger.info("Executing DDL: $sql")
                            statement.execute(sql)
                        }
                    }

                    conn.commit()
                    true
                } catch (e: Exception) {
                    conn.rollback()
                    logger.error("DDL execution failed, rolled back", e)
                    throw e
                } finally {
                    conn.autoCommit = originalAutoCommit
                }
            }
        }.getOrDefault(false)
    }

}
