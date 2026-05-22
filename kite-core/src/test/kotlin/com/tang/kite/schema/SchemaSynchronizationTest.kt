package com.tang.kite.schema

import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.KiteDataSourceFactory
import com.tang.kite.enumeration.IndexOrder
import com.tang.kite.metadata.MetaDataHandlers
import com.tang.kite.schema.synchronization.column.modifycolumn.AccountModifyColumn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SchemaSynchronizationTest {

    val resource = "kite-schema-config.yml"

    val kiteDataSource = KiteDataSourceFactory.build(resource)

    val databaseValue = kiteDataSource.getCurrentDatabase()

    val synchronization = SchemaSynchronization(databaseValue)

    @Test
    fun synchronizeTable() {
        val tableName = "table_account"

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.initialtable")
        val initialTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(initialTable)
        val columns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(3, columns.size)
        assertEquals("Table for account initial", initialTable.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.modifycommentnull")
        val tableCommentNullTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(tableCommentNullTable)
        assertNull(tableCommentNullTable.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.table.modifycomment")
        val tableCommentTable = MetaDataHandlers.getTable(databaseValue, tableName)
        assertNotNull(tableCommentTable)
        assertEquals("Table for account comment", tableCommentTable.comment)
    }


    @Test
    fun synchronizeColumn() {
        val tableName = "column_account"

        synchronization.withPackage("com.tang.kite.schema.synchronization.column.initialcolumn")
        val initialColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(3, initialColumns.size)

        synchronization.withPackage("com.tang.kite.schema.synchronization.column.addcolumn")
        val addColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(6, addColumns.size)
        val addCreateTime = addColumns.find { it.columnName.equals("create_time", ignoreCase = true) }
        val addUpdateTime = addColumns.find { it.columnName.equals("update_time", ignoreCase = true) }
        val addBalance = addColumns.find { it.columnName.equals("balance", ignoreCase = true) }
        assertNotNull(addCreateTime)
        assertNotNull(addUpdateTime)
        assertNotNull(addBalance)
        assertEquals("Create time", addCreateTime.comment)
        assertEquals("Update time", addUpdateTime.comment)
        assertNull(addBalance.comment)

        synchronization.withPackage("com.tang.kite.schema.synchronization.column.modifycolumn")
        val modifyColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(6, modifyColumns.size)
        val modifyCreateTimeColumn = modifyColumns.find { it.columnName.equals("create_time", ignoreCase = true) }
        val modifyUpdateTimeColumn = modifyColumns.find { it.columnName.equals("update_time", ignoreCase = true) }
        val modifyBalanceColumn = modifyColumns.find { it.columnName.equals("balance", ignoreCase = true) }
        assertNotNull(modifyCreateTimeColumn)
        assertNotNull(modifyUpdateTimeColumn)
        assertNotNull(modifyBalanceColumn)
        assertEquals("Register time", modifyCreateTimeColumn.comment)
        assertNull(modifyUpdateTimeColumn.comment)

        SchemaConfig.dropExistingColumns = true
        synchronization.withPackage("com.tang.kite.schema.synchronization.column.dropcolumn")
        SchemaConfig.dropExistingColumns = false
        val dropColumns = MetaDataHandlers.getColumns(databaseValue, tableName)
        assertEquals(3, dropColumns.size)
        val balanceColumn = dropColumns.find { it.columnName.equals("balance", ignoreCase = true) }
        val dropCreateTimeColumnComment = dropColumns.find { it.columnName.equals("create_time", ignoreCase = true) }
        val dropUpdateTimeColumnComment = dropColumns.find { it.columnName.equals("update_time", ignoreCase = true) }
        assertNull(balanceColumn)
        assertNull(dropCreateTimeColumnComment?.comment)
        assertNull(dropUpdateTimeColumnComment?.comment)
    }

    @Test
    fun synchronizeIndex() {
        val tableName = "index_account"

        synchronization.withPackage("com.tang.kite.schema.synchronization.index.initialindex")
        val initialIndexMap = MetaDataHandlers.getIndexes(databaseValue, tableName)
        assertEquals(1, initialIndexMap.size)
        assertTrue { initialIndexMap.values.first().unique }
        assertTrue { initialIndexMap.values.first().primaryKey }

        synchronization.withPackage("com.tang.kite.schema.synchronization.index.addindex")
        val addIndexMap = MetaDataHandlers.getIndexes(databaseValue, tableName)
        assertEquals(3, addIndexMap.size)
        assertTrue { addIndexMap.containsKey("uk_index_account_username_nickname") }
        assertTrue { addIndexMap["uk_index_account_username_nickname"]!!.sorts.contains(IndexOrder.DESC) }
        assertTrue { addIndexMap.containsKey("idx_index_account_username") }
        assertFalse { addIndexMap["idx_index_account_username"]!!.unique }

        SchemaConfig.modifyIndexes = true
        synchronization.withPackage("com.tang.kite.schema.synchronization.index.modifyindex")
        SchemaConfig.modifyIndexes = false
        val modifyIndexMap = MetaDataHandlers.getIndexes(databaseValue, tableName)
        assertEquals(3, modifyIndexMap.size)
        assertTrue { modifyIndexMap.containsKey("idx_index_account_username") }
        assertTrue { modifyIndexMap["idx_index_account_username"]!!.sorts.contains(IndexOrder.DESC) }

        SchemaConfig.dropExistingIndexes = true
        synchronization.withPackage("com.tang.kite.schema.synchronization.index.dropindex")
        SchemaConfig.dropExistingIndexes = false
        val dropIndexMap = MetaDataHandlers.getIndexes(databaseValue, tableName)
        assertEquals(2, dropIndexMap.size)
        assertFalse { dropIndexMap.containsKey("idx_index_account_username") }
    }

    private fun SchemaSynchronization.withPackage(packageName: String) {
        SchemaConfig.scanPackages += packageName
        synchronizeSchema()
        SchemaConfig.scanPackages.clear()
    }

    @Test
    fun test() {
        val columns = SchemaBuilder.buildColumns(AccountModifyColumn::class)
        println()
    }

}
