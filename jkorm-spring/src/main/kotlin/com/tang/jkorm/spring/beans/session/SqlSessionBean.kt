package com.tang.jkorm.spring.beans.session

import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.proxy.SqlSessionProxy
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

}
