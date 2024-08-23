package com.tang.jkorm.spring

import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.mapper.AccountMapper
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
