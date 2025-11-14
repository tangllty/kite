package com.tang.kite.session.factory

import com.tang.kite.config.KiteConfig
import com.tang.kite.datasource.DataSourceFactory
import com.tang.kite.datasource.pooled.PooledDataSourceFactory
import com.tang.kite.io.Resources
import com.tang.kite.session.Configuration
import com.tang.kite.session.factory.defaults.DefaultSqlSessionFactory
import com.tang.kite.sql.factory.defaults.DefaultSqlDialectFactory
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

    init {
        printBanner()
    }

    lateinit var transactionFactory: TransactionFactory

    lateinit var dataSourceFactory: DataSourceFactory

    fun build(resource: String): SqlSessionFactory {
        val inputStream = Resources.getResourceAsStream(resource)
        return build(inputStream)
    }

    fun build(inputStream: InputStream): SqlSessionFactory {
        val datasource = Resources.getDataSourceProperties(inputStream)
        if (::dataSourceFactory.isInitialized.not()) {
            dataSourceFactory = PooledDataSourceFactory(datasource)
        }
        return build(dataSourceFactory.getDataSource())
    }

    fun build(dataSource: DataSource): SqlSessionFactory {
        val connection = dataSource.connection
        val url = connection.metaData.url
        connection.close()
        val sqlDialect = DefaultSqlDialectFactory().newSqlDialect(url)
        if (::transactionFactory.isInitialized.not()) {
            transactionFactory = JdbcTransactionFactory()
        }
        return DefaultSqlSessionFactory(Configuration(dataSource, sqlDialect, transactionFactory))
    }

    private fun printBanner() {
        if (KiteConfig.banner.not()) {
            return
        }

        println("""
              _  __  _____   _______   ______
             | |/ / |_   _| |__   __| |  ____|
             | ' /    | |      | |    | |__
             |  <     | |      | |    |  __|
             | . \   _| |_     | |    | |____
             |_|\_\ |_____|    |_|    |______|    Documentation: https://tangllty.eu.org/

        """.trimIndent())
    }

}
