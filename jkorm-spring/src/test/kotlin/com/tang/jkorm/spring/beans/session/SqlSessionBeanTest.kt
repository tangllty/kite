package com.tang.jkorm.spring.beans.session

import com.tang.jkorm.spring.ApplicationConfig
import com.tang.jkorm.spring.constants.BeanNames
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
class SqlSessionBeanTest {

    @Test
    fun sqlSessionTest() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(BeanNames.SQL_SESSION_FACTORY)
        context.getBean(BeanNames.SQL_SESSION)
    }

}
