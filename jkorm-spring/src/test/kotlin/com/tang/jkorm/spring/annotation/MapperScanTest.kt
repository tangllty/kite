package com.tang.jkorm.spring.annotation

import com.tang.jkorm.spring.ApplicationConfig
import com.tang.jkorm.spring.mapper.AccountJavaMapper
import com.tang.jkorm.spring.mapper.AccountMapper
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
