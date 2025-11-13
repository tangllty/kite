package com.tang.kite.sql.dialect

import com.tang.kite.constants.SqlString.COMMA_SPACE
import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.provider.ProviderType

/**
 * MySQL SQL provider
 *
 * @author Tang
 */
class MysqlDialect : SqlDialect {

    override fun getType(): ProviderType {
        return ProviderType.MYSQL
    }

    override fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause) {
        parameters.add(((limitClause.pageNumber - 1) * limitClause.pageSize))
        parameters.add(limitClause.pageSize)
        sql.append("$LIMIT$QUESTION_MARK$COMMA_SPACE$QUESTION_MARK")
    }

}
