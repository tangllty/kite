package com.tang.kite.spring.beans.session.factory

import com.tang.kite.spring.ApplicationConfig
import com.tang.kite.spring.constants.BeanNames
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
class SqlSessionFactoryBeanTest {

    @Test
    fun sqlSessionFactoryBeanTest() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(BeanNames.SQL_SESSION_FACTORY)
    }

}
