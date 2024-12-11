package com.tang.jkorm.spring.beans

import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.beans.session.SqlSessionBean
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
