package com.tang.jkorm.boot.autoconfigure

import com.tang.jkorm.spring.constants.BeanNames
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.runner.ApplicationContextRunner

/**
 * @author Tang
 */
class JkOrmAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
            .withUserConfiguration(DataSourceAutoConfiguration::class.java, JkOrmAutoConfiguration::class.java)
            .withPropertyValues("jkorm.banner=false")

    @Test
    fun sqlSessionFactory() {
        contextRunner.run { context ->
            assertNotNull(context.getBean("dataSource"))
            assertNotNull(context.getBean(BeanNames.SQL_SESSION_FACTORY))
        }
    }

    @Test
    fun sqlSession() {
        contextRunner.run { context ->
            assertNotNull(context.getBean(BeanNames.SQL_SESSION_FACTORY))
            assertNotNull(context.getBean(BeanNames.SQL_SESSION))
        }
    }

}
