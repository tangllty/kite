package com.tang.jkorm.spring.beans.session.factory

import com.tang.jkorm.datasource.defaults.DefaultDataSourceFactory
import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import javax.sql.DataSource

/**
 * SqlSessionFactory bean
 *
 * @author Tang
 */
class SqlSessionFactoryBean : FactoryBean<SqlSessionFactory>, InitializingBean {

    lateinit var resource: String
    lateinit var dataSource: DataSource
    private lateinit var sqlSessionFactory: SqlSessionFactory

    override fun getObject(): SqlSessionFactory {
        return sqlSessionFactory
    }

    override fun getObjectType(): Class<*> {
        return sqlSessionFactory::class.java
    }

    override fun afterPropertiesSet() {
        sqlSessionFactory = SqlSessionFactoryBuilder().build(dataSource)
    }

    fun afterResourcesSet() {
        val resource = Resources.getResourceAsStream(resource)
        val datasource = Resources.getDataSourceProperties(resource)
        val dataSourceFactory = DefaultDataSourceFactory(datasource)
        dataSource = dataSourceFactory.getDataSource()
    }

}
