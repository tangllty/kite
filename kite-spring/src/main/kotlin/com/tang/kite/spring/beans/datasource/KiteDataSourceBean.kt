package com.tang.kite.spring.beans.datasource

import com.tang.kite.datasource.KiteDataSource
import com.tang.kite.datasource.KiteDataSourceFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import javax.sql.DataSource

/**
 * DataSource bean
 *
 * @author Tang
 */
class KiteDataSourceBean : FactoryBean<KiteDataSource>, InitializingBean {

    private var resource: String? = null

    private var dataSource: DataSource? = null

    private var properties: LinkedHashMap<String, Any?>? = null

    constructor(resource: String) {
        this.resource = resource
    }

    constructor(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    constructor(properties: LinkedHashMap<String, Any?>) {
        this.properties = properties
    }

    private lateinit var kiteDataSource: KiteDataSource

    override fun getObject(): KiteDataSource {
        return kiteDataSource
    }

    override fun getObjectType(): Class<*> {
        return kiteDataSource::class.java
    }

    override fun afterPropertiesSet() {
        this.kiteDataSource = when {
            properties.isNullOrEmpty().not() -> KiteDataSourceFactory.build(properties!!)
            dataSource != null -> KiteDataSourceFactory.build(dataSource!!)
            resource.isNullOrBlank().not() -> KiteDataSourceFactory.build(resource!!)
            else -> throw IllegalStateException("DataSource or Properties must not be null")
        }
    }

}
