package com.tang.kite.sql.provider.mysql

import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * MySQL SQL provider
 *
 * @author Tang
 */
class MysqlSqlProvider : AbstractSqlProvider() {

    override fun providerType(): DatabaseType {
        return DatabaseType.MYSQL
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
        return "$LIMIT$QUESTION_MARK$COMMA_SPACE$QUESTION_MARK"
    }

}
