package com.tang.kite.sql.dialect

import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.provider.ProviderType

/**
 * @author Tang
 */
class H2SqlDialect : SqlDialect {

    override fun getType(): ProviderType {
        return ProviderType.H2
    }

    override fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause) {
        parameters.add((limitClause.pageNumber - 1) * limitClause.pageSize)
        parameters.add(limitClause.pageSize)
        sql.append("$LIMIT$QUESTION_MARK$OFFSET$QUESTION_MARK")
    }

}
