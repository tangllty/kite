package com.tang.kite.spring.beans

import com.tang.kite.mapper.BaseMapper
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.beans.session.SqlSessionBean
import org.springframework.beans.factory.FactoryBean

/**
 * @author Tang
 */
class MapperFactoryBean<M : BaseMapper<T>, T : Any>(

    private val mapperInterface: Class<M>,

    private val sqlSessionFactory: SqlSessionFactory

) : FactoryBean<M> {

    override fun getObject(): M {
        return SqlSessionBean(sqlSessionFactory).getMapper(mapperInterface)
    }

    override fun getObjectType(): Class<*> {
        return mapperInterface
    }

}
