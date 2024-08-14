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
        assertEquals("insert into account (username,password) values ('tang','123456')", sql)
    }

    @Test
    fun insertSelective() {
        val account = Account(username = "tang", password = "123456")
        val sql = sqlProvider.insertSelective(account)
        assertEquals("insert into account (username,password) values ('tang','123456')", sql)
    }

    @Test
    fun update() {
        val account = Account(id = 1, username = "tang", password = "123456")
        val sql = sqlProvider.update(account)
        assertEquals("update account set username='tang',password='123456' where id = 1", sql)
    }

    @Test
    fun updateSelective() {
        val account = Account(id = 1, password = "123456")
        val sql = sqlProvider.updateSelective(account)
        assertEquals("update account set password='123456' where id = 1", sql)
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
        assertEquals("select * from account where username='tang'", sql)
    }

}
