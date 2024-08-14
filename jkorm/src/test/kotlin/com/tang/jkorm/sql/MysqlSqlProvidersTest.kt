package com.tang.jkorm.sql

import com.tang.jkorm.session.entity.Account
import com.tang.jkorm.sql.provider.mysql.MysqlSqlProvider
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class MysqlSqlProvidersTest {

    private val sqlProvider = MysqlSqlProvider()

    @Test
    fun insert() {
        val account = Account(username = "tang", password = "123456")
        val sql = sqlProvider.insert(account)
        assertEquals("insert into account (username, password) values ('tang', '123456')", sql)
    }

    @Test
    fun insertSelective() {
        val account = Account(username = "tang", password = "123456")
        val sql = sqlProvider.insertSelective(account)
        assertEquals("insert into account (username, password) values ('tang', '123456')", sql)
    }

    @Test
    fun update() {
        val account = Account(id = 1, username = "tang", password = "123456")
        val sql = sqlProvider.update(account)
        assertEquals("update account set username = 'tang', password = '123456' where id = 1", sql)
    }

    @Test
    fun updateSelective() {
        val account = Account(id = 1, password = "123456")
        val sql = sqlProvider.updateSelective(account)
        assertEquals("update account set password = '123456' where id = 1", sql)
    }

    @Test
    fun delete() {
        val account = Account(id = 1)
        val sql = sqlProvider.delete(Account::class.java, account)
        assertEquals("delete from account where id = 1", sql)
    }

    @Test
    fun select() {
        val sql = sqlProvider.select(Account::class.java, null)
        assertEquals("select * from account", sql)
    }

    @Test
    fun selectCondition() {
        val account = Account(username = "tang")
        val sql = sqlProvider.select(Account::class.java, account)
        assertEquals("select * from account where username = 'tang'", sql)
    }

    @Test
    fun count() {
        val sql = sqlProvider.count(Account::class.java, null)
        assertEquals("select count(*) from account", sql)
    }

    @Test
    fun countCondition() {
        val account = Account(username = "tang")
        val sql = sqlProvider.count(Account::class.java, account)
        assertEquals("select count(*) from account where username = 'tang'", sql)
    }

    @Test
    fun paginate() {
        val sql = sqlProvider.paginate(Account::class.java, null, emptyArray(), 1, 5)
        assertEquals("select * from account limit 0,5", sql)
    }

    @Test
    fun paginateCondition() {
        val account = Account(username = "tang")
        val sql = sqlProvider.paginate(Account::class.java, account, emptyArray(), 1, 5)
        assertEquals("select * from account where username = 'tang' limit 0,5", sql)
    }

    @Test
    fun paginateOrderBy() {
        val sql = sqlProvider.paginate(Account::class.java, null, arrayOf("id" to false), 1, 5)
        assertEquals("select * from account order by id desc limit 0,5", sql)
    }

    @Test
    fun paginateOrderByCondition() {
        val account = Account(username = "tang")
        val sql = sqlProvider.paginate(Account::class.java, account, arrayOf("id" to false), 1, 5)
        assertEquals("select * from account where username = 'tang' order by id desc limit 0,5", sql)
    }

}
