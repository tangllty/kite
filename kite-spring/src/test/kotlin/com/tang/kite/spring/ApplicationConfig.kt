package com.tang.kite.spring

import com.tang.kite.datasource.KiteDataSource
import com.tang.kite.session.SqlSession
import com.tang.kite.session.factory.SqlSessionFactory
import com.tang.kite.spring.annotation.MapperScan
import com.tang.kite.spring.beans.datasource.KiteDataSourceBean
import com.tang.kite.spring.beans.session.SqlSessionBean
import com.tang.kite.spring.beans.session.factory.SqlSessionFactoryBean
import com.tang.kite.spring.constants.BeanNames
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

/**
 * @author Tang
 */
@EnableTransactionManagement
@Configuration
@MapperScan(["com.tang.kite.spring.mapper"])
open class ApplicationConfig {

    @Bean(BeanNames.KITE_DATA_SOURCE)
    open fun kiteDataSource(): KiteDataSource {
        val kiteDataSourceBean = KiteDataSourceBean("kite-config.yml")
        kiteDataSourceBean.afterPropertiesSet()
        return kiteDataSourceBean.getObject()
    }

    @Bean(BeanNames.SQL_SESSION_FACTORY)
    open fun sqlSessionFactory(@Qualifier(BeanNames.KITE_DATA_SOURCE) kiteDataSource: KiteDataSource): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean(kiteDataSource)
        sqlSessionFactoryBean.afterPropertiesSet()
        return sqlSessionFactoryBean.getObject()
    }

    @Bean(BeanNames.SQL_SESSION)
    open fun sqlSession(@Qualifier(BeanNames.SQL_SESSION_FACTORY) sqlSessionFactory: SqlSessionFactory): SqlSession {
        return SqlSessionBean(sqlSessionFactory).sqlSession
    }

    @Bean
    open fun platformTransactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    open fun transactionTemplate(transactionManager: PlatformTransactionManager): TransactionTemplate {
        return TransactionTemplate(transactionManager)
    }

}
