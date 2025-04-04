package com.tang.kite.spring.proxy

import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.utils.SqlSessions
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * SqlSession proxy
 *
 * @author Tang
 */
class SqlSessionProxy(private val sqlSessionFactory: SqlSessionFactory) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        if (method.name != "execute") {
            return method.invoke(sqlSessionFactory, *(args ?: emptyArray()))
        }
        val sqlSession = SqlSessions.openSession(sqlSessionFactory)
        val result = method.invoke(sqlSession, *(args ?: emptyArray()))
        sqlSession.commit()
        SqlSessions.closeSession(sqlSessionFactory, sqlSession)
        return result
    }

}
