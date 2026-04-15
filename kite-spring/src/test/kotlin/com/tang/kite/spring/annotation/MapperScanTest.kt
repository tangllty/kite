package com.tang.kite.spring.annotation

import com.tang.kite.spring.ApplicationContext
import com.tang.kite.spring.mapper.AccountJavaMapper
import com.tang.kite.spring.mapper.AccountMapper
import kotlin.test.Test

/**
 * @author Tang
 */
class MapperScanTest {

    @Test
    fun test() {
        ApplicationContext.context.getBean(AccountMapper::class.java)
        ApplicationContext.context.getBean(AccountJavaMapper::class.java)
    }

}
