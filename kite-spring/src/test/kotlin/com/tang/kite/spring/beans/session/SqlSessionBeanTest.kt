package com.tang.kite.spring.beans.session

import com.tang.kite.spring.ApplicationContext
import com.tang.kite.spring.constants.BeanNames
import kotlin.test.Test

/**
 * @author Tang
 */
class SqlSessionBeanTest {

    @Test
    fun sqlSessionTest() {
        ApplicationContext.context.getBean(BeanNames.SQL_SESSION_FACTORY)
        ApplicationContext.context.getBean(BeanNames.SQL_SESSION)
    }

}
