package com.tang.kite.sql.provider.postgresql

import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.provider.ProviderType

/**
 * PostgreSQL SQL provider
 *
 * @author Tang
 */
class PostgresqlSqlProvider : AbstractSqlProvider() {

    override fun providerType(): ProviderType {
        return ProviderType.POSTGRESQL
    }

}
