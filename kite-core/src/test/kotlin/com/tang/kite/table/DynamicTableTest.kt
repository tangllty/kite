package com.tang.kite.table

import com.tang.kite.BaseDataTest
import com.tang.kite.annotation.Table
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.utils.Reflects
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test cases for dynamic table name functionality
 *
 * @author Tang
 */
class DynamicTableTest : BaseDataTest() {

    @Table("account")
    class Account

    @Table(value = "order", alias = "o")
    class Order

    @Test
    fun testTableContextStackBehavior() {
        withContext {
            TableContext.push("account_1")
            assertEquals("account_1", TableContext.current())

            TableContext.push("account_2")
            assertEquals("account_2", TableContext.current())

            TableContext.pop()
            assertEquals("account_1", TableContext.current())

            TableContext.pop()
        }
    }

    @Test
    fun testTableManagerWithBlock() {
        withContext {
            val result = TableManager.with("account_table") {
                assertEquals("account_table", TableContext.current())
                TableContext.current()
            }
            assertEquals("account_table", result)
        }
    }

    @Test
    fun testWithTableExtensionFunction() {
        withContext {
            val result = withTable("partition_table") {
                assertEquals("partition_table", TableContext.current())
                TableContext.current()
            }
            assertEquals("partition_table", result)
        }
    }

    @Test
    fun testReflectsGetTableNameWithContext() {
        withContext {
            val defaultTableName = Reflects.getTableName(Account::class.java)
            assertEquals("account", defaultTableName)

            withTable("account_custom") {
                val contextTableName = Reflects.getTableName(Account::class.java)
                assertEquals("account_custom", contextTableName)
            }

            assertEquals("account", Reflects.getTableName(Account::class.java))
        }
    }

    @Test
    fun testNestedTableContexts() {
        withContext {
            withTable("table_1") {
                assertEquals("table_1", TableContext.current())
                assertEquals("table_1", Reflects.getTableName(Account::class.java))

                withTable("table_2") {
                    assertEquals("table_2", TableContext.current())
                    assertEquals("table_2", Reflects.getTableName(Account::class.java))

                    withTable("table_3") {
                        assertEquals("table_3", TableContext.current())
                        assertEquals("table_3", Reflects.getTableName(Account::class.java))
                    }

                    assertEquals("table_2", TableContext.current())
                }

                assertEquals("table_1", TableContext.current())
            }
        }
    }

    @Test
    fun testTableNamePriorityContextOverridesDefault() {
        withContext {
            assertEquals("account", Reflects.getTableName(Account::class.java))
            assertEquals("order", Reflects.getTableName(Order::class.java))

            withTable("custom_account") {
                assertEquals("custom_account", Reflects.getTableName(Account::class.java))
                assertEquals("custom_account", Reflects.getTableName(Order::class.java))
            }

            assertEquals("account", Reflects.getTableName(Account::class.java))
            assertEquals("order", Reflects.getTableName(Order::class.java))
        }
    }

    @Test
    fun testWithSuffixMethod() {
        withContext {
            TableManager.withSuffix(Account::class, "suffix") {
                assertEquals("account_suffix", TableContext.current())
            }
        }
    }

    @Test
    fun testWithTransformMethod() {
        withContext {
            TableManager.withTransform(Account::class, { it + "_suffix" }) {
                assertEquals("account_suffix", TableContext.current())
            }
        }
    }

    @Test
    fun testWithTopLevelSuffixMethod() {
        withContext {
            withTableSuffix(Account::class, "suffix") {
                assertEquals("account_suffix", TableContext.current())
            }
        }
    }

    @Test
    fun testWithTopLevelTransformMethod() {
        withContext {
            withTableTransform(Account::class, { it + "_suffix" }) {
                assertEquals("account_suffix", TableContext.current())
            }
        }
    }

    @Test
    fun testBaseMapperWithMethod() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        withContext {
            accountMapper.with("account_table") {
                assertEquals("account_table", TableContext.current())
            }
            assertNull(TableContext.current())
        }
        session.close()
    }

    @Test
    fun testBaseMapperWithSuffixMethod() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        withContext {
            accountMapper.withSuffix("suffix") {
                assertEquals("account_suffix", TableContext.current())
            }
            assertNull(TableContext.current())
        }
        session.close()
    }

    @Test
    fun testBaseMapperWithTransformMethod() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        withContext {
            accountMapper.withTransform({ "${it}_suffix" }) {
                assertEquals("account_suffix", TableContext.current())
            }
        }
        session.close()
    }

    @Test
    fun testBaseMapperNestedWithContext() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        withContext {
            accountMapper.with("account_1") {
                assertEquals("account_1", TableContext.current())
                accountMapper.withSuffix("2") {
                    assertEquals("account_1_2", TableContext.current())
                    accountMapper.withTransform({ "${it}_3" }) {
                        assertEquals("account_1_2_3", TableContext.current())
                    }
                    assertEquals("account_1_2", TableContext.current())
                }
                assertEquals("account_1", TableContext.current())
            }
        }
        session.close()
    }

    private fun withContext(block: () -> Unit) {
        assertNull(TableContext.current())
        block()
        assertNull(TableContext.current())
    }

}
