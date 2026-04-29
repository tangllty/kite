package com.tang.kite.metadata

import com.tang.kite.BaseDataTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class MetaDataHandlersTest : BaseDataTest() {

    @Test
    fun getTable() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val table = MetaDataHandlers.getTable(connection, "account")
        connection.close()
        assertNotNull(table)
        assertEquals("account", table.tableName.lowercase())
    }

    @Test
    fun getTables() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val tables = MetaDataHandlers.getTables(connection)
        connection.close()
        assertNotNull(tables)
        assertTrue { tables.isNotEmpty() }
        assertTrue { tables.map { it.tableName.lowercase() }.contains("account") }
    }

    @Test
    fun getColumns() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val columns = MetaDataHandlers.getColumns(connection, "account")
        connection.close()
        assertNotNull(columns)
        assertTrue { columns.map { it.columnName.lowercase() }.contains("id") }
    }

    @Test
    fun getPrimaryKeys() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val primaryKeys = MetaDataHandlers.getPrimaryKeys(connection, "account")
        connection.close()
        assertNotNull(primaryKeys)
        assertContentEquals(listOf("id"), primaryKeys.map { it.lowercase() })
    }

    @Test
    fun getUniqueKeys() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val uniqueKeys = MetaDataHandlers.getUniqueKeys(connection, "account")
        connection.close()
        assertNotNull(uniqueKeys)
        assertContentEquals(listOf("id"), uniqueKeys.map { it.lowercase() })
    }

    @Test
    fun getUniqueIndexes() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val uniqueIndexes = MetaDataHandlers.getUniqueIndexes(connection, "account")
        connection.close()
        assertNotNull(uniqueIndexes)
    }

    @Test
    fun getIndexes() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val indexes = MetaDataHandlers.getIndexes(connection, "account")
        connection.close()
        assertNotNull(indexes)
    }

    @Test
    fun getTableTypes() {
        val connection = sqlSessionFactory.openSession().getConnection()
        val tableTypes = MetaDataHandlers.getTableTypes(connection.metaData)
        connection.close()
        assertNotNull(tableTypes)
        assertTrue { tableTypes.isNotEmpty() }
    }

}
