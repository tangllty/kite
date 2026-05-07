package com.tang.kite.session.factory

import com.tang.kite.config.KiteConfig
import com.tang.kite.config.schema.SchemaConfig
import com.tang.kite.datasource.KiteDataSource
import com.tang.kite.datasource.KiteDataSourceFactory
import com.tang.kite.schema.SchemaSynchronization
import com.tang.kite.session.factory.defaults.DefaultSqlSessionFactory
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

    var transactionFactory: TransactionFactory

    constructor() : this(JdbcTransactionFactory)

    constructor(transactionFactory: TransactionFactory) {
        this.transactionFactory = transactionFactory
    }

    fun build(resource: String): SqlSessionFactory {
        return build(KiteDataSourceFactory.build(resource))
    }

    fun build(inputStream: InputStream): SqlSessionFactory {
        return build(KiteDataSourceFactory.build(inputStream))
    }

    fun build(dataSource: DataSource): SqlSessionFactory {
        return build(KiteDataSourceFactory.build(dataSource))
    }

    fun build(kiteDataSource: KiteDataSource): SqlSessionFactory {
        if (SchemaConfig.enabled) {
            val schemaSynchronization = SchemaSynchronization(kiteDataSource.getCurrentDatabase())
            schemaSynchronization.synchronizeSchema()
        }
        return DefaultSqlSessionFactory(kiteDataSource)
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
