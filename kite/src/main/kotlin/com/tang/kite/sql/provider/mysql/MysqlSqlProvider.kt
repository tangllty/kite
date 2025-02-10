package com.tang.kite.sql.provider.mysql

import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.provider.ProviderType

/**
 * MySQL SQL provider
 *
 * @author Tang
 */
class MysqlSqlProvider : AbstractSqlProvider() {

    override fun providerType(): ProviderType {
        return ProviderType.MYSQL
    }

    override fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long) {
        sql.append(LIMIT).append(QUESTION_MARK).append(COMMA_SPACE).append(QUESTION_MARK)
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
    }

}
