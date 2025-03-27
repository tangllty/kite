package com.tang.kite.session.factory

import com.tang.kite.config.KiteConfig
import com.tang.kite.datasource.defaults.DefaultDataSourceFactory
import com.tang.kite.io.Resources
import com.tang.kite.session.Configuration
import com.tang.kite.session.factory.defaults.DefaultSqlSessionFactory
import com.tang.kite.sql.defaults.DefaultSqlProviderFactory
import com.tang.kite.transaction.TransactionFactory
import com.tang.kite.transaction.jdbc.JdbcTransactionFactory
import java.io.InputStream
import javax.sql.DataSource

/**
 * To build a SqlSessionFactory instance.
 *
 * @author Tang
 */
class SqlSessionFactoryBuilder {

    lateinit var transactionFactory: TransactionFactory

    fun build(resource: String): SqlSessionFactory {
        val inputStream = Resources.getResourceAsStream(resource)
        return build(inputStream)
    }

    fun build(inputStream: InputStream): SqlSessionFactory {
        val datasource = Resources.getDataSourceProperties(inputStream)
        val dataSourceFactory = DefaultDataSourceFactory(datasource)
        return build(dataSourceFactory.getDataSource())
    }

    fun build(dataSource: DataSource): SqlSessionFactory {
        val url = dataSource.connection.use { it.metaData.url }
        val sqlProvider = DefaultSqlProviderFactory().newSqlProvider(url)
        if (!::transactionFactory.isInitialized) {
            transactionFactory = JdbcTransactionFactory()
        }
        printBanner()
        return DefaultSqlSessionFactory(Configuration(dataSource, sqlProvider, transactionFactory))
    }

    fun printBanner() {
        if (!KiteConfig.banner) {
            return
        }

        println("""
                ___  ___  __    ________  ________  _____ ______
               |\  \|\  \|\  \ |\   __  \|\   __  \|\   _ \  _   \
               \ \  \ \  \/  /|\ \  \|\  \ \  \|\  \ \  \\\__\ \  \
             __ \ \  \ \   ___  \ \  \\\  \ \   _  _\ \  \\|__| \  \
            |\  \\_\  \ \  \\ \  \ \  \\\  \ \  \\  \\ \  \    \ \  \
            \ \________\ \__\\ \__\ \_______\ \__\\ _\\ \__\    \ \__\
             \|________|\|__| \|__|\|_______|\|__|\|__|\|__|     \|__|      Documentation: https://tangllty.eu.org/

        """.trimIndent())
    }

}
