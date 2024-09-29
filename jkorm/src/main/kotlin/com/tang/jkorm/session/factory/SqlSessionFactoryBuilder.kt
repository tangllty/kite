package com.tang.jkorm.session.factory

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.datasource.defaults.DefaultDataSourceFactory
import com.tang.jkorm.io.Resources
import com.tang.jkorm.session.Configuration
import com.tang.jkorm.session.factory.defaults.DefaultSqlSessionFactory
import com.tang.jkorm.sql.defaults.DefaultSqlProviderFactory
import java.io.InputStream
import javax.sql.DataSource

/**
 * @author Tang
 */
class SqlSessionFactoryBuilder {

    fun build(inputStream: InputStream): SqlSessionFactory {
        val datasource = Resources.getDataSourceProperties(inputStream)
        val dataSourceFactory = DefaultDataSourceFactory(datasource)
        return build(dataSourceFactory.getDataSource())
    }

    fun build(dataSource: DataSource): SqlSessionFactory {
        val url = dataSource.connection.use { it.metaData.url }
        val sqlProvider = DefaultSqlProviderFactory().newSqlProvider(url)
        printBanner()
        return DefaultSqlSessionFactory(Configuration(dataSource, sqlProvider))
    }

    fun printBanner() {
        if (!JkOrmConfig.INSTANCE.banner) {
            return
        }

        println("""
                ___  ___  __    ________  ________  _____ ______
               |\  \|\  \|\  \ |\   __  \|\   __  \|\   _ \  _   \
               \ \  \ \  \/  /|\ \  \|\  \ \  \|\  \ \  \\\__\ \  \
             __ \ \  \ \   ___  \ \  \\\  \ \   _  _\ \  \\|__| \  \
            |\  \\_\  \ \  \\ \  \ \  \\\  \ \  \\  \\ \  \    \ \  \
            \ \________\ \__\\ \__\ \_______\ \__\\ _\\ \__\    \ \__\
             \|________|\|__| \|__|\|_______|\|__|\|__|\|__|     \|__|

        """.trimIndent())
    }

}
