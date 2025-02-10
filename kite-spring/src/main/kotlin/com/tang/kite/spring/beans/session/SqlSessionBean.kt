package com.tang.kite.spring.beans.session

import com.tang.kite.proxy.MapperProxyFactory
import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.proxy.SqlSessionProxy
import java.lang.reflect.Proxy

/**
 * SqlSession bean
 *
 * @author Tang
 */
class SqlSessionBean(sqlSessionFactory: SqlSessionFactory) {

    val sqlSession: SqlSession

    init {
        val classLoader = SqlSessionFactory::class.java.classLoader
        val classes = arrayOf(SqlSession::class.java)
        sqlSession = Proxy.newProxyInstance(classLoader, classes, SqlSessionProxy(sqlSessionFactory)) as SqlSession
    }

    fun <T> getMapper(clazz: Class<T>): T {
        val mapperProxyFactory = MapperProxyFactory(clazz)
        return mapperProxyFactory.newInstance(sqlSession)
    }

}
