package com.tang.kite.spring.proxy

import com.tang.kite.spring.ApplicationContext
import com.tang.kite.spring.mapper.AccountJavaMapper
import com.tang.kite.spring.mapper.AccountMapper
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * @author Tang
 */
class SqlSessionProxyTest {

    @Test
    fun sqlSessionTest() {
        val accountMapper = ApplicationContext.context.getBean(AccountMapper::class.java)
        val accountJavaMapper = ApplicationContext.context.getBean(AccountJavaMapper::class.java)
        val select1 = accountMapper.select()
        val select2 = accountMapper.select()
        val select3 = accountJavaMapper.select()
        assertNotNull(select1)
        assertNotNull(select2)
        assertNotNull(select3)
    }

}
