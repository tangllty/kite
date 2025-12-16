package com.tang.kite.boot.autoconfigure

import com.tang.kite.config.KiteConfig
import com.tang.kite.spring.constants.BeanNames
import org.springframework.beans.factory.getBean
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author Tang
 */
class KiteAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(DataSourceAutoConfiguration::class.java, KiteAutoConfiguration::class.java)
        .withPropertyValues(
            "kite.banner=false",
            "kite.page.page-number=2",
            "kite.sql.sql-logging=false",
        )

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

    @Test
    fun properties() {
        contextRunner.run { context ->
            val kiteProperties = context.getBean<KiteProperties>()
            assertNotNull(kiteProperties)
            assertEquals(kiteProperties.banner, KiteConfig.banner)
            assertEquals(kiteProperties.page.pageNumber, KiteConfig.page.pageNumber)
            assertEquals(kiteProperties.page.pageSize, KiteConfig.page.pageSize)
        }
    }

}
