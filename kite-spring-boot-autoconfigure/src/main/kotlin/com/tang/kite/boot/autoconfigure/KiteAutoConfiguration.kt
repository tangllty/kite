package com.tang.kite.boot.autoconfigure

import com.tang.kite.datasource.KiteDataSource
import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.beans.datasource.KiteDataSourceBean
import com.tang.kite.spring.beans.session.SqlSessionBean
import com.tang.kite.spring.beans.session.factory.SqlSessionFactoryBean
import com.tang.kite.spring.constants.BeanNames
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import javax.sql.DataSource

/**
 * Kite auto configuration
 *
 * @author Tang
 */
@AutoConfiguration(afterName = [
    "org.springframework.boot.autoconfigure.jdbc.DataSourceProperties",
    "org.springframework.boot.jdbc.autoconfigure.DataSourceProperties",
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
    "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration"
])
@ConditionalOnClass(KiteDataSourceBean::class, SqlSessionFactoryBean::class, SqlSessionBean::class)
@EnableConfigurationProperties(KiteProperties::class)
open class KiteAutoConfiguration {

    constructor(kiteProperties: KiteProperties) {
        kiteProperties.applyProperties()
    }

    @Bean(BeanNames.KITE_DATA_SOURCE)
    @ConditionalOnMissingBean
    open fun kiteDataSource(kiteProperties: KiteProperties, dataSourceProvider: ObjectProvider<DataSource>): KiteDataSource {
        val kiteDataSourceBean = if (kiteProperties.datasource.isEmpty().not()) {
            KiteDataSourceBean(kiteProperties.datasource)
        } else {
            KiteDataSourceBean(dataSourceProvider.getIfAvailable()!!)
        }
        kiteDataSourceBean.afterPropertiesSet()
        return kiteDataSourceBean.getObject()
    }

    @DependsOn(BeanNames.KITE_DATA_SOURCE)
    @Bean(BeanNames.SQL_SESSION_FACTORY)
    @ConditionalOnMissingBean
    open fun sqlSessionFactory(kiteDataSource: KiteDataSource): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean(kiteDataSource)
        sqlSessionFactoryBean.afterPropertiesSet()
        return sqlSessionFactoryBean.getObject()
    }

    @DependsOn(BeanNames.SQL_SESSION_FACTORY)
    @Bean(BeanNames.SQL_SESSION)
    @ConditionalOnMissingBean
    open fun sqlSession(@Qualifier(BeanNames.SQL_SESSION_FACTORY) sqlSessionFactory: SqlSessionFactory): SqlSession {
        return SqlSessionBean(sqlSessionFactory).sqlSession
    }

}
