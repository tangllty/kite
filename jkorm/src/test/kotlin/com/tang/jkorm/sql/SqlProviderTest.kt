package com.tang.jkorm.sql

import com.tang.jkorm.session.entity.Account
import com.tang.jkorm.session.entity.JavaAccount
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class SqlProviderTest {

    @Test
    fun insert() {
        val account = Account(username = "tang", password = "123456")
        val sql = SqlProvider.insert(account)
        assertEquals("insert into account (id,username,password) values (null,'tang','123456')", sql.lowercase())
    }

    @Test
    fun insertSelective() {
        val account = Account(username = "tang", password = "123456")
        val sql = SqlProvider.insertSelective(account)
        assertEquals("insert into account (username,password) values ('tang','123456')", sql.lowercase())
    }

    @Test
    fun update() {
        val account = Account(id = 1, username = "tang", password = "123456")
        val sql = SqlProvider.update(account)
        assertEquals("update account set username='tang',password='123456' where id = 1", sql.lowercase())
    }

    @Test
    fun updateSelective() {
        val account = Account(id = 1, password = "123456")
        val sql = SqlProvider.updateSelective(account)
        assertEquals("update account set password='123456' where id = 1", sql.lowercase())
    }

}
