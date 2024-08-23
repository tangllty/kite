package com.tang.jkorm.session

import com.tang.jkorm.BaseDataTest
import com.tang.jkorm.session.entity.Account
import com.tang.jkorm.session.mapper.AccountMapper
import com.tang.jkorm.session.mapper.JavaAccountMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author Tang
 */
class SqlSessionTest : BaseDataTest() {

    @Test
    fun getMapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertSelective() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang")
        val rows = accountMapper.insertSelective(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(JavaAccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertAccount() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAccount(account)
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsertAccount() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(JavaAccountMapper::class.java)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAccount(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun multiSqlSession() {
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
        session1.close()
        session2.close()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun multiInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper1 = session.getMapper(AccountMapper::class.java)
        val account1 = Account(username = "tang1", password = "123456")
        val account2 = Account(username = "tang2", password = "123456")
        val rows1 = accountMapper1.insert(account1)
        val rows2 = accountMapper1.insert(account2)
        session.commit()
        session.close()
        assertEquals(1, rows1)
        assertEquals(1, rows2)
    }

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang", password = "654321")
        val rows = accountMapper.update(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1, username = "tang")
        val rows = accountMapper.updateSelective(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(id = 1)
        val rows = accountMapper.delete(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val rows = accountMapper.deleteById(1L)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun selectAll() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val accounts = accountMapper.select()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun selectById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = accountMapper.selectById(1)
        session.close()
        assertNotNull(account)
    }

    @Test
    fun selectCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin")
        val accounts = accountMapper.select(account)
        session.close()
        assertTrue(accounts.isNotEmpty())
    }

    @Test
    fun count () {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val count = accountMapper.count()
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun countCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin")
        val count = accountMapper.count(account)
        session.close()
        assertNotEquals(0, count)
    }

    @Test
    fun paginate() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5)
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, Account(username = "tang"))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOderBy() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, arrayOf("id" to false))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun paginateOrderByCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val page = accountMapper.paginate(2, 5, arrayOf("id" to false), Account(username = "tang"))
        session.close()
        assertNotEquals(0, page.total)
    }

    @Test
    fun sqlInjection() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class.java)
        val account = Account(username = "admin' or '1'='1")
        val list = accountMapper.select(account)
        session.close()
        assertEquals(0, list.size)
    }

}
