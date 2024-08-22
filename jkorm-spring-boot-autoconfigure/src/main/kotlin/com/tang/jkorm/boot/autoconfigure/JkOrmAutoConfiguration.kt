package com.tang.jkorm.boot.autoconfigure

import com.tang.jkorm.annotation.Slf4j
import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.beans.session.SqlSessionBean
import com.tang.jkorm.spring.beans.session.factory.SqlSessionFactoryBean
import com.tang.jkorm.spring.constants.BeanNames
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

/**
 * JkOrm auto configuration
 *
 * @author Tang
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(SqlSessionFactoryBean::class, SqlSessionBean::class)
open class JkOrmAutoConfiguration {

    @Bean(BeanNames.SQL_SESSION_FACTORY)
    open fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.dataSource = dataSource
        sqlSessionFactoryBean.afterPropertiesSet()
        return sqlSessionFactoryBean.getObject()
    }

    @Bean(BeanNames.SQL_SESSION)
    open fun sqlSession(@Qualifier(BeanNames.SQL_SESSION_FACTORY) sqlSessionFactory: SqlSessionFactory): SqlSession {
        return SqlSessionBean(sqlSessionFactory).sqlSession
    }

}
