package com.tang.kite.spring.annotation

import com.tang.kite.spring.ApplicationConfig
import com.tang.kite.spring.mapper.AccountJavaMapper
import com.tang.kite.spring.mapper.AccountMapper
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Tang
 */
class MapperScanTest {

    @Test
    fun test() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        context.getBean(AccountMapper::class.java)
        context.getBean(AccountJavaMapper::class.java)
    }

}
