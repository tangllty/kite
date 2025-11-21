package com.tang.kite.spring.proxy

import com.tang.kite.proxy.MapperProxyFactory
import com.tang.kite.session.SqlSession
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
        // Special-case the execute method which should be executed inside a managed session
        if (method.name == "execute") {
            val sqlSession = SqlSessions.openSession(sqlSessionFactory)
            try {
                val result = method.invoke(sqlSession, *(args ?: emptyArray()))
                sqlSession.commit()
                return result
            } finally {
                SqlSessions.closeSession(sqlSessionFactory, sqlSession)
            }
        }

        // Treat close as a no-op on the proxy itself (closing a proxy bean shouldn't try to invoke
        // interface methods on the factory which causes IllegalArgumentException)
        if (method.name == "close") {
            return Unit
        }

        // Handle Object methods (toString, equals, hashCode)
        if (method.declaringClass == Any::class.java) {
            return method.invoke(this, *(args ?: emptyArray()))
        }

        // Provide Mapper instances that use this proxy as the SqlSession
        if (method.name == "getMapper") {
            val mapperClass = args?.get(0) as Class<*>
            val factory = MapperProxyFactory(mapperClass)
            @Suppress("UNCHECKED_CAST")
            return factory.newInstance(proxy as SqlSession)
        }

        // For other SqlSession methods (commit, rollback, etc.) delegate to a real session
        val sqlSession = SqlSessions.openSession(sqlSessionFactory)
        try {
            return method.invoke(sqlSession, *(args ?: emptyArray()))
        } finally {
            SqlSessions.closeSession(sqlSessionFactory, sqlSession)
        }
    }

}
