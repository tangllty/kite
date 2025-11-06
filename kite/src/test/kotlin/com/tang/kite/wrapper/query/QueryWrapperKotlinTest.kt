package com.tang.kite.wrapper.query

import com.tang.kite.session.entity.Account
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tang
 */
class QueryWrapperKotlinTest : QueryWrapperTest, QueryWrapperConstants() {

    @Test
    override fun selectAll() {
        val sqlStatement = QueryWrapper<Account>()
            .select()
            .from(Account::class)
            .build()
            .getSqlStatement()
        assertEquals(selectFromAccount, sqlStatement.sql)
    }

    @Test
    override fun selectSpecificColumns() {
        val sqlStatement = QueryWrapper<Account>()
            .select(Account::id, Account::username, Account::createTime)
            .from(Account::class)
            .build()
            .getSqlStatement()
        assertEquals("select id, username, create_time from account", sqlStatement.sql)
    }

    @Test
    override fun selectWhere() {
        val sqlStatement = QueryWrapper<Account>()
            .select()
            .from(Account::class)
            .where()
            .eq(Account::id, 1)
            .eq(Account::username, "tang")
            .build()
            .getSqlStatement()
        assertEquals("$selectFromAccount where id = ? and username = ?", sqlStatement.sql)
        assertEquals("$selectFromAccount where id = 1 and username = 'tang'", sqlStatement.getActualSql())
    }

    @Test
    override fun selectGroupBy() {
        val sqlStatement = QueryWrapper<Account>()
            .select()
            .from(Account::class)
            .groupBy(Account::username)
            .build()
            .getSqlStatement()
        assertEquals("$selectFromAccount group by username", sqlStatement.sql)
    }

    @Test
    override fun selectHaving() {
        val sqlStatement = QueryWrapper<Account>()
            .select()
            .from(Account::class)
            .groupBy(Account::username)
            .having()
            .eq(Account::username, "Tang")
            .build()
            .getSqlStatement()
        assertEquals("$selectFromAccount group by username having username = ?", sqlStatement.sql)
        assertEquals("$selectFromAccount group by username having username = 'Tang'", sqlStatement.getActualSql())
    }

    @Test
    override fun selectOrderBy() {
        val sqlStatement = QueryWrapper<Account>()
            .select()
            .from(Account::class)
            .orderBy(Account::id)
            .build()
            .getSqlStatement()
        assertEquals("$selectFromAccount order by id asc", sqlStatement.sql)
    }

}
