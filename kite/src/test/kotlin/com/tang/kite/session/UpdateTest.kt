package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.session.entity.Account
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.wrapper.update.UpdateWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author Tang
 */
class UpdateTest : BaseDataTest() {

    @Test
    fun update() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(id = 1, username = "tang", password = "654321")
        val rows = accountMapper.update(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateCondition() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(username = "tang", password = "654321")
        val condition = Account(id = 1)
        val rows = accountMapper.update(account, condition)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(id = 1, username = "tang")
        val rows = accountMapper.updateSelective(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val updateWrapper = UpdateWrapper.create<Account>()
            .from(Account::class.java)
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::id, 1, true)
            .build()
        val rows = accountMapper.updateWrapper(updateWrapper)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapperShort() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val updateWrapper = UpdateWrapper.create<Account>()
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .eq(Account::id, 1, true)
            .build()
        val rows = accountMapper.updateWrapper(updateWrapper)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.updateWrapper()
            .from(Account::class.java)
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .where()
            .eq(Account::id, 1)
            .update()
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapperPostShort() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.updateWrapper()
            .set(Account::username, "tang")
            .set("password", "123456", false)
            .eq(Account::id, 1)
            .update()
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun updateWrapperFill() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.updateWrapper()
            .set(Account::username, "tang")
            .set(Account::password, null)
            .set(Account::updateTime, LocalDateTime.of(2000, 1, 1, 1, 1))
            .eq(Account::id, 1)
            .update()
        session.commit()
        session.close()
        assertEquals(1, rows)
        val account = accountMapper.selectById(1)
        assertNotNull(account?.updateTime)
    }

    @Test
    fun batchUpdate() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = arrayOf(
            Account(id = 1, username = "admin", password = "123456", updateTime = LocalDateTime.now(), balance = BigDecimal(1000.00)),
            Account(id = 4, username = "tang", password = "123456", updateTime = LocalDateTime.now(), balance = BigDecimal(2000.00))
        )
        val rows = accountMapper.batchUpdate(accounts)
        session.rollback()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun batchUpdateSelective() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val accounts = arrayOf(
            Account(id = 1, username = "admin"),
            Account(id = 4, username = "tang")
        )
        val rows = accountMapper.batchUpdateSelective(accounts)
        session.rollback()
        session.close()
        assertEquals(2, rows)
    }

}
