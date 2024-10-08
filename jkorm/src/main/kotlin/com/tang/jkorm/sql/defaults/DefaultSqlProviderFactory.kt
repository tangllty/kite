package com.tang.jkorm.sql.defaults

import com.tang.jkorm.config.JkOrmConfig
import com.tang.jkorm.sql.SqlProviderFactory
import com.tang.jkorm.sql.provider.SqlProvider

/**
 * Default SQL provider factory
 *
 * @author Tang
 */
class DefaultSqlProviderFactory : SqlProviderFactory {

    override fun newSqlProvider(url: String): SqlProvider {
        val urlProviderMap = JkOrmConfig.INSTANCE.urlProviders
        val provider = urlProviderMap.entries.find { url.contains(":${it.key}:") }?.value
        return provider ?: throw IllegalArgumentException("Unsupported database url: $url")
    }

}
