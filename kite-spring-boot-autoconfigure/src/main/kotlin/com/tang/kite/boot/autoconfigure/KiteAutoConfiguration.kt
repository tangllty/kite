package com.tang.kite.boot.autoconfigure

import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.beans.session.SqlSessionBean
import com.tang.kite.spring.beans.session.factory.SqlSessionFactoryBean
import com.tang.kite.spring.constants.BeanNames
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import javax.sql.DataSource

/**
 * Kite auto configuration
 *
 * @author Tang
 */
@AutoConfiguration(after = [DataSourceAutoConfiguration::class, DataSourceProperties::class])
@ConditionalOnClass(SqlSessionFactoryBean::class, SqlSessionBean::class)
@ConditionalOnSingleCandidate(DataSource::class)
@EnableConfigurationProperties(KiteProperties::class)
open class KiteAutoConfiguration {

    constructor(kiteProperties: KiteProperties) {
        kiteProperties.apply()
    }

    @DependsOn(BeanNames.DATA_SOURCE)
    @Bean(BeanNames.SQL_SESSION_FACTORY)
    @ConditionalOnMissingBean
    open fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean(dataSource)
        sqlSessionFactoryBean.afterPropertiesSet()
        return sqlSessionFactoryBean.getObject()
    }

    @Bean(BeanNames.SQL_SESSION)
    @ConditionalOnMissingBean
    open fun sqlSession(@Qualifier(BeanNames.SQL_SESSION_FACTORY) sqlSessionFactory: SqlSessionFactory): SqlSession {
        return SqlSessionBean(sqlSessionFactory).sqlSession
    }

}
