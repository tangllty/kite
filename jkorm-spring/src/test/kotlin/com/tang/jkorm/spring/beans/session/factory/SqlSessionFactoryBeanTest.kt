package com.tang.jkorm.spring.beans.session.factory

import com.tang.jkorm.spring.ApplicationConfig
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
class SqlSessionFactoryBeanTest {

    @Test
    fun sqlSessionFactoryBeanTest() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.beanDefinitionNames.forEach { println(it) }
        context.getBean("sqlSessionFactory")
    }

}
