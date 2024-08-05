package com.tang.jkorm.proxy

import com.tang.jkorm.session.SqlSession
import java.lang.reflect.Proxy

/**
 * @author Tang
 */
class MapperProxyFactory<T>(private val mapperInterface: Class<T>) {

    fun newInstance(sqlSession: SqlSession): T {
        val classLoader = mapperInterface.classLoader
        val classes = arrayOf(mapperInterface)
        val proxy = Proxy.newProxyInstance(classLoader, classes, MapperProxy(sqlSession, mapperInterface))
        return mapperInterface.cast(proxy)
    }

}
