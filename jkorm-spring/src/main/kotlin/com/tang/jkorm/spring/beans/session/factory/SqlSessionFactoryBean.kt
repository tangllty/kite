package com.tang.jkorm.spring.beans.session.factory

import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.session.factory.SqlSessionFactoryBuilder
import com.tang.jkorm.spring.transaction.SpringTransactionFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import javax.sql.DataSource

/**
 * SqlSessionFactory bean
 *
 * @author Tang
 */
class SqlSessionFactoryBean(private var dataSource: DataSource) : FactoryBean<SqlSessionFactory>, InitializingBean {

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
        sqlSessionFactory = builder.build(dataSource)
    }

}
