package com.tang.jkorm.spring.proxy

import com.tang.jkorm.session.factory.SqlSessionFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * SqlSession proxy
 *
 * @author Tang
 */
class SqlSessionProxy(private val sqlSessionFactory: SqlSessionFactory) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        val sqlSession = sqlSessionFactory.openSession()
        val result = method.invoke(sqlSession, *(args ?: emptyArray()))
        return result
    }

}
