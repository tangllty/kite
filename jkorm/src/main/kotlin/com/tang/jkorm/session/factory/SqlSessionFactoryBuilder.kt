package com.tang.jkorm.session.factory

import com.tang.jkorm.datasource.defaults.DefaultDataSourceFactory
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.factory.defaults.DefaultSqlSessionFactory
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * @author Tang
 */
class SqlSessionFactoryBuilder {

    fun build(inputStream: InputStream): SqlSessionFactory {
        val yaml = Yaml().load<Map<String, Map<String, Map<String, String>>>>(inputStream)
        val jkorm = yaml["jkorm"]
        val datasource = jkorm!!["datasource"] as Map<String, String>
        val dataSourceFactory = DefaultDataSourceFactory(datasource)
        return DefaultSqlSessionFactory(Configuration(dataSourceFactory.getDataSource()))
    }

}
