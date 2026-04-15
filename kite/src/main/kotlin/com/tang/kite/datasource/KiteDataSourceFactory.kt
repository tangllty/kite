package com.tang.kite.datasource

import com.tang.kite.datasource.pooled.PooledDataSourceFactory
import com.tang.kite.datasource.pooled.PooledProperties
import com.tang.kite.io.Resources
import com.tang.kite.sql.enumeration.DatabaseType
import com.tang.kite.sql.factory.defaults.DefaultSqlDialectFactory
import java.io.InputStream
import javax.sql.DataSource

/**
 * @author Tang
 */
object KiteDataSourceFactory {

    fun build(resource: String): KiteDataSource {
        return build(Resources.getResourceAsStream(resource))
    }

    fun build(inputStream: InputStream): KiteDataSource {
        val pooledProperties = Resources.getDataSourceProperties(inputStream, PooledProperties::class.java)
        return build(pooledProperties)
    }

    fun build(properties: LinkedHashMap<String, Any?>): KiteDataSource {
        val pooledProperties = Resources.getDataSourceProperties(properties, PooledProperties::class.java)
        return build(pooledProperties)
    }

    fun build(pooledProperties: Map<String, PooledProperties>): KiteDataSource {
        val databaseMap = pooledProperties.mapValues {
            val dataSource = PooledDataSourceFactory(it.value).getDataSource()
            val databaseType = DatabaseType.getDatabaseType(dataSource)
            val sqlDialect = DefaultSqlDialectFactory.createSqlDialect(databaseType)
            DatabaseValue(dataSource, databaseType, sqlDialect)
        }
        return KiteDataSource(databaseMap)
    }

    fun build(dataSource: DataSource): KiteDataSource {
        val databaseType = DatabaseType.getDatabaseType(dataSource)
        val sqlDialect = DefaultSqlDialectFactory.createSqlDialect(databaseType)
        return KiteDataSource(mapOf("default" to DatabaseValue(dataSource, databaseType, sqlDialect)))
    }

}
