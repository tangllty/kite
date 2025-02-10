package com.tang.kite.session.factory

import com.tang.kite.io.Resources
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tang
 */
class SqlSessionFactoryBuilderTest {

    @Test
    fun build() {
        val inputStream = Resources.getResourceAsStream("kite-config.yml")
        val sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
        assertNotNull(sqlSessionFactory)
    }

}
