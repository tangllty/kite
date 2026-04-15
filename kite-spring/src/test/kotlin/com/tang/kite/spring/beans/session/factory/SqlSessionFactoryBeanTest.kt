package com.tang.kite.spring.beans.session.factory

import com.tang.kite.spring.ApplicationContext
import com.tang.kite.spring.constants.BeanNames
import kotlin.test.Test

/**
 * @author Tang
 */
class SqlSessionFactoryBeanTest {

    @Test
    fun sqlSessionFactoryBeanTest() {
        ApplicationContext.context.getBean(BeanNames.SQL_SESSION_FACTORY)
    }

}
