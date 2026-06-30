package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.session.mapper.AccountMapper
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * @author Tang
 */
class SqlSessionTest : BaseDataTest() {

    @Test
    fun getMapper() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun createTable() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        accountMapper.createTable("account_test")
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun createTableWithSuffix() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        accountMapper.createTableWithSuffix("test")
        session.close()
        assertNotNull(accountMapper)
    }

    @Test
    fun createTableWithTransform() {
        val session = sqlSessionFactory.openSession()
        val accountMapper = session.getMapper(AccountMapper::class)
        accountMapper.createTableWithTransform { it + "_test" }
        session.close()
        assertNotNull(accountMapper)
    }

}
