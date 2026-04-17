package com.tang.kite.datasource

import com.tang.kite.transaction.TransactionFactory
import com.tang.kite.transaction.jdbc.JdbcTransactionFactory
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author Tang
 */
class KiteDataSource : AbstractDataSource {

    private val dataSourceKey: String

    val database: DatabaseValue

    val transactionFactory: TransactionFactory

    constructor(dataSourceKey: String, database: DatabaseValue) : this(mapOf(dataSourceKey to database))

    constructor(databaseMap: Map<String, DatabaseValue>) : this(databaseMap, JdbcTransactionFactory())

    constructor(databaseMap: Map<String, DatabaseValue>, transactionFactory: TransactionFactory) {
        this.dataSourceKey = databaseMap.keys.first()
        this.database = databaseMap.values.first()
        this.transactionFactory = transactionFactory
        DataSourceRegistry.registerBatch(databaseMap)
    }

    fun getCurrentDataSourceKey(): String {
        val key = DataSourceContext.current()
        if (key != null) {
            return key
        }
        return dataSourceKey
    }

    fun getCurrentDatabase(): DatabaseValue {
        val key = DataSourceContext.current()
        if (key != null) {
            return DataSourceRegistry.get(key)
        }
        return database
    }

    fun getCurrentDataSource(): DataSource {
        return getCurrentDatabase().dataSource
    }

    override fun getConnection(): Connection {
        return getCurrentDataSource().connection
    }

    override fun getConnection(username: String?, password: String?): Connection {
        return getCurrentDataSource().getConnection(username, password)
    }

}
