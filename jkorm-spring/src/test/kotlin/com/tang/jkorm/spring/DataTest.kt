package com.tang.jkorm.spring

import com.tang.jkorm.spring.mapper.AccountMapper
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.test.Test
import kotlin.test.assertNotEquals

/**
 * @author Tang
 */
class DataTest: BaseDataTest() {

    @Test
    fun select() {
        val context = AnnotationConfigApplicationContext(ApplicationConfig::class.java)
        val accountMapper = context.getBean(AccountMapper::class.java)
        val select = accountMapper.select()
        assertNotEquals(0, select.size)
    }

}
