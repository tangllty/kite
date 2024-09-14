package com.tang.jkorm.sql.provider.postgresql

import com.tang.jkorm.sql.provider.AbstractSqlProvider
import com.tang.jkorm.sql.provider.ProviderType

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
