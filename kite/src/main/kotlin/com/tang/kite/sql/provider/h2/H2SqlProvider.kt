package com.tang.kite.sql.provider.h2

import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.provider.ProviderType

/**
 * @author Tang
 */
class H2SqlProvider : AbstractSqlProvider() {

    override fun providerType(): ProviderType {
        return ProviderType.H2
    }

    override fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long) {
        sql.append(" LIMIT ? OFFSET ?")
        parameters.add(pageSize)
        parameters.add((pageNumber - 1) * pageSize)
    }

}
