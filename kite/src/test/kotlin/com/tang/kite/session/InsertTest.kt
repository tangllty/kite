package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.session.entity.Account
import com.tang.kite.session.entity.Role
import com.tang.kite.session.mapper.AccountJavaMapper
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.session.mapper.RoleMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
class InsertTest : BaseDataTest() {

    @Test
    fun insert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertSelective() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "tang")
        val rows = accountMapper.insertSelective(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsert() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountJavaMapper::class)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insert(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertAccount() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(
            username = "tang",
            password = "123456",
            createTime = LocalDateTime.now(),
            updateTime = null,
            balance = BigDecimal(100.00)
        )
        val rows = accountMapper.insert(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun javaInsertAccount() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountJavaMapper::class)
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
        val accountMapper1 = session1.getMapper(AccountMapper::class)
        val accountMapper2 = session2.getMapper(AccountMapper::class)
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
        val accountMapper1 = session.getMapper(AccountMapper::class)
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
    fun insertValues() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = mutableListOf<Account>()
        for (i in 1..100) {
            accounts.add(Account(username = "tang$i", password = "123456"))
        }
        val rows = accountMapper.insertValues(accounts, 30)
        session.commit()
        session.close()
        assertEquals(100, rows)
    }

    @Test
    fun batchInsert() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = listOf(
            Account(username = "tang1", password = "123456"),
            Account(username = "tang2", password = "123456", balance = BigDecimal(2000))
        )
        val rows = accountMapper.batchInsert(accounts)
        session.commit()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun batchInsertSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = listOf(
            Account(username = "tang1", password = "123456"),
            Account(username = "tang2", balance = BigDecimal(2000))
        )
        val rows = accountMapper.batchInsertSelective(accounts)
        session.commit()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun batchInsertSize() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = mutableListOf<Account>()
        for (i in 1..100) {
            accounts.add(Account(username = "tang$i", password = "123456"))
        }
        val rows = accountMapper.batchInsert(accounts, 30)
        session.commit()
        session.close()
        assertEquals(100, rows)
    }

    @Test
    fun batchInsertSelectiveSize() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = mutableListOf<Account>()
        repeat(100) {
            accounts.add(Account(username = "tang$it"))
        }
        val rows = accountMapper.batchInsertSelective(accounts, 30)
        session.commit()
        session.close()
        assertEquals(100, rows)
    }

    @Test
    fun insertAccountFill() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(
            username = "tang",
            password = "123456",
            balance = BigDecimal(100.00)
        )
        val rows = accountMapper.insert(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
        val account2 = accountMapper.selectById(account.id!!)
        assertNotNull(account2?.createTime)
    }

    @Test
    fun insertAnnotation() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAnnotation(account)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun insertAnnotationParam() {
        val session = sqlSessionFactory.openSession(true)
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "tang", password = "123456")
        val rows = accountMapper.insertAnnotation(account, Account(), "test")
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun autoFillId() {
        val session = sqlSessionFactory.openSession()
        val roleMapper = session.getMapper(RoleMapper::class)
        val role = Role(name = "tang")
        val rows = roleMapper.insert(role)
        session.commit()
        session.close()
        assertEquals(1, rows)
    }

}
