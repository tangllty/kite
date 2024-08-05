package com.tang.jkorm.session

import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.entity.Account
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import com.tang.jkorm.session.mapper.AccountMapper
import com.tang.jkorm.session.mapper.JavaAccountMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class SqlSessionTest {

    @Test
    fun getMapper() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession()
        val accountMapper = openSession.getMapper(AccountMapper::class.java)
        assertNotNull(accountMapper)
    }

    @Test
    fun insert() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession(true)
        val accountMapper = openSession.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        assertEquals(1, rows)
    }

    @Test
    fun insertSelective() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession(true)
        val accountMapper = openSession.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang")
        val rows = accountMapper.insertSelective(account)
        assertEquals(1, rows)
    }

    @Test
    fun javaInsert() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession(true)
        val accountMapper = openSession.getMapper(JavaAccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        assertEquals(1, rows)
    }

    @Test
    fun insertAccount() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession(true)
        val accountMapper = openSession.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAccount(account)
        assertEquals(1, rows)
    }

    @Test
    fun javaInsertAccount() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val openSession = sqlSessionFactory.openSession()
        val accountMapper = openSession.getMapper(JavaAccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAccount(account)
        openSession.rollback()
        assertEquals(1, rows)
    }

    @Test
    fun testMultiSqlSession() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session1 = sqlSessionFactory.openSession()
        val session2 = sqlSessionFactory.openSession()
        val accountMapper1 = session1.getMapper(AccountMapper::class.java)
        val accountMapper2 = session2.getMapper(AccountMapper::class.java)
        val account1 = Account(username = "tang1", password = "123456")
        val account2 = Account(username = "tang2", password = "123456")
        val rows1 = accountMapper1.insert(account1)
        val rows2 = accountMapper2.insert(account2)
        session1.commit()
        session2.commit()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun testMultiInsert() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session1 = sqlSessionFactory.openSession()
        val accountMapper1 = session1.getMapper(AccountMapper::class.java)
        val account1 = Account(username = "tang1", password = "123456")
        val account2 = Account(username = "tang2", password = "123456")
        val rows1 = accountMapper1.insert(account1)
        val rows2 = accountMapper1.insert(account2)
        session1.commit()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun testUpdate() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang", password = "654321")
        val rows = accountMapper.update(account)
        session.rollback()
        assertEquals(1, rows)
    }

    @Test
    fun testUpdateSelective() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang")
        val rows = accountMapper.updateSelective(account)
        session.rollback()
        assertEquals(1, rows)
    }

    @Test
    fun testDelete() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1)
        val rows = accountMapper.delete(account)
        session.rollback()
        assertEquals(1, rows)
    }

    @Test
    fun testDeleteById() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val rows = accountMapper.deleteById(1L)
        session.rollback()
        assertEquals(1, rows)
    }

    @Test
    fun testSelectAll() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.select()
        assertNotNull(accounts)
    }

    @Test
    fun testSelectById() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = accountMapper.selectById(1)
        assertNotNull(account)
    }

}
