package com.tang.kite.spring

import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.mapper.AccountMapper
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.test.Test

/**
 * @author Tang
 */
class BeanTest {

    @Test
    fun sqlSessionFactory() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(SqlSessionFactory::class.java)
    }

    @Test
    fun sqlSession() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(SqlSessionFactory::class.java)
    }

    @Test
    fun accountMapper() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(AccountMapper::class.java)
    }

}
