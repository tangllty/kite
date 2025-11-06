package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.session.entity.Account
import com.tang.kite.session.mapper.AccountMapper
import com.tang.kite.wrapper.delete.DeleteWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Tang
 */
class DeleteTest : BaseDataTest() {

    @Test
    fun delete() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val account = Account(id = 1)
        val rows = accountMapper.delete(account)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteById() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.deleteById(1)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteByIds() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.deleteByIds(listOf(1L, 2L))
        session.rollback()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun deleteByIdArray() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.deleteByIds(arrayOf(1L, 2L))
        session.rollback()
        session.close()
        assertEquals(2, rows)
    }

    @Test
    fun deleteWrapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val deleteWrapper = DeleteWrapper.create<Account>()
            .from(Account::class)
            .eq(Account::id, 1)
            .isNotNull(Account::username)
            .build()
        val rows = accountMapper.deleteWrapper(deleteWrapper)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteWrapperShort() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val deleteWrapper = DeleteWrapper.create<Account>()
            .eq(Account::id, 1)
            .isNotNull(Account::username)
            .build()
        val rows = accountMapper.deleteWrapper(deleteWrapper)
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteWrapperPost() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.deleteWrapper()
            .from(Account::class)
            .eq(Account::id, 1)
            .isNotNull(Account::username)
            .delete()
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

    @Test
    fun deleteWrapperPostShort() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        val rows = accountMapper.deleteWrapper()
            .eq(Account::id, 1)
            .isNotNull(Account::username)
            .delete()
        session.rollback()
        session.close()
        assertEquals(1, rows)
    }

}
