package com.tang.jkorm.spring

import com.tang.jkorm.session.SqlSession
import com.tang.jkorm.session.factory.SqlSessionFactory
import com.tang.jkorm.spring.annotation.MapperScan
import com.tang.jkorm.spring.beans.session.SqlSessionBean
import com.tang.jkorm.spring.beans.session.factory.SqlSessionFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Tang
 */
@Configuration
@MapperScan(["com.tang.jkorm.spring.mapper"])
open class ApplicationConfig {

    @Bean
    open fun sqlSessionFactory(): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.resource = "jkorm-config.yml"
        sqlSessionFactoryBean.afterPropertiesSet()
        return sqlSessionFactoryBean.getObject()
    }

    @Bean
    open fun sqlSession(sqlSessionFactory: SqlSessionFactory): SqlSession {
        return SqlSessionBean(sqlSessionFactory).sqlSession
    }

}
