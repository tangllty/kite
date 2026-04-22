package com.tang.kite.proxy

import com.tang.kite.mapper.BaseMapper
import com.tang.kite.session.SqlSession
import java.lang.reflect.Proxy

/**
 * @author Tang
 */
class MapperProxyFactory<M : BaseMapper<T>, T : Any>(private val mapperInterface: Class<M>) {

    fun newInstance(sqlSession: SqlSession): M {
        val classLoader = mapperInterface.classLoader
        val classes = arrayOf(mapperInterface)
        val proxy = Proxy.newProxyInstance(classLoader, classes, MapperProxy(sqlSession, mapperInterface))
        return mapperInterface.cast(proxy)
    }

}
