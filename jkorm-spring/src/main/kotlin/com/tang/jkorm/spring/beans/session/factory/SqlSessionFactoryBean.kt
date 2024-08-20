package com.tang.jkorm.spring.beans.session.factory

import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean

/**
 * SqlSessionFactory bean
 *
 * @author Tang
 */
class SqlSessionFactoryBean : FactoryBean<SqlSessionFactory>, InitializingBean {

    lateinit var resource: String
    private lateinit var sqlSessionFactory: SqlSessionFactory

    override fun getObject(): SqlSessionFactory {
        return sqlSessionFactory
    }

    override fun getObjectType(): Class<*> {
        return sqlSessionFactory::class.java
    }

    override fun afterPropertiesSet() {
        val resource = Resources.getResourceAsStream(resource)
        sqlSessionFactory = SqlSessionFactoryBuilder().build(resource)
    }

}
