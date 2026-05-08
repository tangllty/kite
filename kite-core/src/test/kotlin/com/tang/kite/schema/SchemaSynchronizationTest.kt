package com.tang.kite.schema

import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.KiteDataSourceFactory
import com.tang.kite.metadata.MetaDataHandlers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * @author Tang
 */
class SchemaSynchronizationTest {

    @Test
    fun synchronizeSchema() {
        val resource = "kite-schema-config.yml"
        val kiteDataSource = KiteDataSourceFactory.build(resource)
        val databaseValue = kiteDataSource.getCurrentDatabase()
        val tableName = "account"
        val synchronization = SchemaSynchronization(databaseValue)

        assertNull(MetaDataHandlers.getTable(databaseValue, tableName))

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.initial")
        val initialTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(initialTable)
        var columns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(3, columns.size)
        assertEquals("Table for account initial", initialTable.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.tablecommentnull")
        val tableCommentNullTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(tableCommentNullTable)
        assertNull(tableCommentNullTable.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.tablecomment")
        val tableCommentTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(tableCommentTable)
        assertEquals("Table for account comment", tableCommentTable.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.column.addcolumn")
        columns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(6, columns.size)
        val createTimeColumn = columns.find { it.columnName.equals("create_time", ignoreCase = true) }
        assertNotNull(createTimeColumn)
        assertEquals("Create time", createTimeColumn.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.column.modifycolumn")
        columns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(6, columns.size)
        val modifyCreateTimeColumn = columns.find { it.columnName.equals("create_time", ignoreCase = true) }
        val modifyUpdateTimeColumn = columns.find { it.columnName.equals("update_time", ignoreCase = true) }
        assertNotNull(modifyCreateTimeColumn)
        assertNotNull(modifyUpdateTimeColumn)
        assertEquals("Register time", modifyCreateTimeColumn.comment)
        assertNull(modifyUpdateTimeColumn.comment)

        SchemaConfig.dropExistingColumns = true
        synchronization.withPackage("com.tang.kite.schema.synchronization.column.dropcolumn")
        SchemaConfig.dropExistingColumns = false
        columns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(5, columns.size)
        val balanceColumn = columns.find { it.columnName.equals("balance", ignoreCase = true) }
        val dropCreateTimeColumnComment = columns.find { it.columnName.equals("create_time", ignoreCase = true) }
        val dropUpdateTimeColumnComment = columns.find { it.columnName.equals("update_time", ignoreCase = true) }
        assertNull(balanceColumn)
        assertNull(dropCreateTimeColumnComment?.comment)
        assertNull(dropUpdateTimeColumnComment?.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.index.addindex")
        val indexMap = MetaDataHandlers.getIndexes(databaseValue, tableName)
        assertEquals(1, indexMap.size)
    }

    private fun SchemaSynchronization.withPackage(packageName: String) {
        SchemaConfig.scanPackages += packageName
        synchronizeSchema()
        SchemaConfig.scanPackages.clear()
    }

}
