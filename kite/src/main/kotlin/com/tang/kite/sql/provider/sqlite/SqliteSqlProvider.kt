package com.tang.kite.sql.provider.sqlite

import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.provider.ProviderType

/**
 * SQLite SQL provider
 *
 * @author Tang
 */
class SqliteSqlProvider : AbstractSqlProvider() {

    override fun providerType(): ProviderType {
        return ProviderType.SQLITE
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
        return "$LIMIT$QUESTION_MARK$OFFSET$QUESTION_MARK"
    }

}
