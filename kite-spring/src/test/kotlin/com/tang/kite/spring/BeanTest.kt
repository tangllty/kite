package com.tang.kite.spring

import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.mapper.AccountMapper
import kotlin.test.Test

/**
 * @author Tang
 */
class BeanTest {

    @Test
    fun sqlSessionFactory() {
        ApplicationContext.context.getBean(SqlSessionFactory::class.java)
    }

    @Test
    fun sqlSession() {
        ApplicationContext.context.getBean(SqlSession::class.java)
    }

    @Test
    fun accountMapper() {
        ApplicationContext.context.getBean(AccountMapper::class.java)
    }

}
