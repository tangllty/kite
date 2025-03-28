package com.tang.kite.spring.beans

import com.tang.kite.datasource.pooled.PooledDataSourceFactory
import com.tang.kite.io.Resources
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import javax.sql.DataSource

/**
 * DataSource bean
 *
 * @author Tang
 */
class DataSourceBean(private var resource: String) : FactoryBean<DataSource>, InitializingBean {

    private lateinit var dataSource: DataSource

    override fun getObject(): DataSource {
        return dataSource
    }

    override fun getObjectType(): Class<*> {
        return dataSource::class.java
    }

    override fun afterPropertiesSet() {
        val resource = Resources.getResourceAsStream(resource)
        val datasource = Resources.getDataSourceProperties(resource)
        val dataSourceFactory = PooledDataSourceFactory(datasource)
        dataSource = dataSourceFactory.getDataSource()
    }

}
