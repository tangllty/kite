package com.tang.kite.sql.defaults

import com.tang.kite.config.KiteConfig
import com.tang.kite.sql.SqlProviderFactory
import com.tang.kite.sql.provider.SqlProvider

/**
 * Default SQL provider factory
 *
 * @author Tang
 */
class DefaultSqlProviderFactory : SqlProviderFactory {

    override fun newSqlProvider(url: String): SqlProvider {
        val urlProviderMap = KiteConfig.urlProviders
        val provider = urlProviderMap.entries.find { url.contains(":${it.key}:") }?.value
        return provider ?: throw IllegalArgumentException("Unsupported database url: $url")
    }

}
