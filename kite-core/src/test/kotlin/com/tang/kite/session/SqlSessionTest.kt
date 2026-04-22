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

}
