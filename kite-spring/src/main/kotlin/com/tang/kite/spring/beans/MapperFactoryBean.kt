package com.tang.kite.spring.beans

import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.beans.session.SqlSessionBean
import org.springframework.beans.factory.FactoryBean

/**
 * @author Tang
 */
class MapperFactoryBean<T>(

    private val mapperInterface: Class<T>,

    private val sqlSessionFactory: SqlSessionFactory

) : FactoryBean<T> {

    override fun getObject(): T {
        return SqlSessionBean(sqlSessionFactory).getMapper(mapperInterface)
    }

    override fun getObjectType(): Class<*> {
        return mapperInterface
    }

}
