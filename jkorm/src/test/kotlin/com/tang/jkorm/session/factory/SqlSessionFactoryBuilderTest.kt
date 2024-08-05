package com.tang.jkorm.session.factory

import com.tang.jkorm.io.Resources
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class SqlSessionFactoryBuilderTest {

    @Test
    fun build() {
        val inputStream = Resources.getResourceAsStream("jkorm-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        assertNotNull(sqlSessionFactory)
    }

}
