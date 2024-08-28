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
        val statement = sqlProvider.insert(account)
        assertEquals("insert into account (username, password, create_time, update_time, balance) values (?, ?, ?, ?, ?)", statement.sql)
        assertEquals("insert into account (username, password, create_time, update_time, balance) values ('tang', '123456', NULL, NULL, NULL)", statement.getActualSql())
    }

    @Test
    fun insertSelective() {
        val account = Account(username = "tang", password = "123456")
        val statement = sqlProvider.insertSelective(account)
        assertEquals("insert into account (username, password) values (?, ?)", statement.sql)
        assertEquals("insert into account (username, password) values ('tang', '123456')", statement.getActualSql())
    }

    @Test
    fun update() {
        val account = Account(id = 1, username = "tang", password = "123456")
        val statement = sqlProvider.update(account)
        assertEquals("update account set username = ?, password = ?, create_time = ?, update_time = ?, balance = ? where id = ?", statement.sql)
        assertEquals("update account set username = 'tang', password = '123456', create_time = NULL, update_time = NULL, balance = NULL where id = 1", statement.getActualSql())
    }

    @Test
    fun updateSelective() {
        val account = Account(id = 1, password = "123456")
        val statement = sqlProvider.updateSelective(account)
        assertEquals("update account set password = ? where id = ?", statement.sql)
        assertEquals("update account set password = '123456' where id = 1", statement.getActualSql())
    }

    @Test
    fun delete() {
        val account = Account(id = 1, username = "tang")
        val statement = sqlProvider.delete(Account::class.java, account)
        assertEquals("delete from account where id = ? and username = ?", statement.sql)
        assertEquals("delete from account where id = 1 and username = 'tang'", statement.getActualSql())
    }

    @Test
    fun deleteById() {
        val account = Account(id = 1)
        val statement = sqlProvider.delete(Account::class.java, account)
        assertEquals("delete from account where id = ?", statement.sql)
        assertEquals("delete from account where id = 1", statement.getActualSql())
    }

    @Test
    fun select() {
        val statement = sqlProvider.select(Account::class.java, null)
        assertEquals("select * from account", statement.sql)
    }

    @Test
    fun selectCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.select(Account::class.java, account)
        assertEquals("select * from account where username = ?", statement.sql)
        assertEquals("select * from account where username = 'tang'", statement.getActualSql())
    }

    @Test
    fun count() {
        val statement = sqlProvider.count(Account::class.java, null)
        assertEquals("select count(*) from account", statement.sql)
    }

    @Test
    fun countCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.count(Account::class.java, account)
        assertEquals("select count(*) from account where username = ?", statement.sql)
        assertEquals("select count(*) from account where username = 'tang'", statement.getActualSql())
    }

    @Test
    fun paginate() {
        val statement = sqlProvider.paginate(Account::class.java, null, emptyArray(), 1, 5)
        assertEquals("select * from account limit ?, ?", statement.sql)
        assertEquals("select * from account limit 0, 5", statement.getActualSql())
    }

    @Test
    fun paginateCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.paginate(Account::class.java, account, emptyArray(), 1, 5)
        assertEquals("select * from account where username = ? limit ?, ?", statement.sql)
        assertEquals("select * from account where username = 'tang' limit 0, 5", statement.getActualSql())
    }

    @Test
    fun paginateOrderBy() {
        val statement = sqlProvider.paginate(Account::class.java, null, arrayOf("id" to false), 1, 5)
        assertEquals("select * from account order by id desc limit ?, ?", statement.sql)
        assertEquals("select * from account order by id desc limit 0, 5", statement.getActualSql())
    }

    @Test
    fun paginateOrderByCondition() {
        val account = Account(username = "tang")
        val statement = sqlProvider.paginate(Account::class.java, account, arrayOf("id" to false), 1, 5)
        assertEquals("select * from account where username = ? order by id desc limit ?, ?", statement.sql)
        assertEquals("select * from account where username = 'tang' order by id desc limit 0, 5", statement.getActualSql())
    }

}
