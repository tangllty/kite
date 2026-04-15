package com.tang.kite.spring.beans.session.factory

import com.tang.kite.datasource.KiteDataSource
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.session.factory.SqlSessionFactoryBuilder
import com.tang.kite.spring.transaction.SpringTransactionFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean

/**
 * SqlSessionFactory bean
 *
 * @author Tang
 */
class SqlSessionFactoryBean(private var kiteDataSource: KiteDataSource) : FactoryBean<SqlSessionFactory>, InitializingBean {

    private lateinit var sqlSessionFactory: SqlSessionFactory

    override fun getObject(): SqlSessionFactory {
        return sqlSessionFactory
    }

    override fun getObjectType(): Class<*> {
        return sqlSessionFactory::class.java
    }

    override fun afterPropertiesSet() {
        val builder = SqlSessionFactoryBuilder()
        builder.transactionFactory = SpringTransactionFactory()
        sqlSessionFactory = builder.build(kiteDataSource)
    }

}
