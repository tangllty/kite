package com.tang.kite.spring.proxy

import com.tang.kite.spring.ApplicationConfig
import com.tang.kite.spring.BaseDataTest
import com.tang.kite.spring.mapper.AccountJavaMapper
import com.tang.kite.spring.mapper.AccountMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
class SqlSessionProxyTest : BaseDataTest() {

    @Test
    fun sqlSessionTest() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        val accountMapper = context.getBean(AccountMapper::class.java)
        val accountJavaMapper = context.getBean(AccountJavaMapper::class.java)
        val select1 = accountMapper.select()
        val select2 = accountMapper.select()
        val select3 = accountJavaMapper.select()
        assertNotNull(select1)
        assertNotNull(select2)
        assertNotNull(select3)
    }

}
