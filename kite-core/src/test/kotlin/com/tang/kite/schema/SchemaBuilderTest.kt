package com.tang.kite.schema

import com.tang.kite.schema.builder.AccountBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SchemaBuilderTest {

    @Test
    fun buildEntity() {
        val entity = SchemaBuilder.buildEntity(AccountBuilder::class)
        assertEquals(entity.table.toString(), "account")
    }

    @Test
    fun buildColumns() {
        val columns = SchemaBuilder.buildColumns(AccountBuilder::class)
        assertEquals(columns.size, 7)
        assertTrue(columns.map { it.columnName }.contains("id"))
    }

    @Test
    fun buildIndexes() {
        val indexes = SchemaBuilder.buildIndexes(AccountBuilder::class)
        assertTrue { indexes.map { it.indexName }.contains("idx_account_username") }
        assertTrue { indexes.map { it.indexName }.contains("uk_account_username_nickname") }
    }

}
