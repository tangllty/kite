package com.tang.kite.session

import com.tang.kite.BaseDataTest
import com.tang.kite.session.mapper.AccountMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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

}
