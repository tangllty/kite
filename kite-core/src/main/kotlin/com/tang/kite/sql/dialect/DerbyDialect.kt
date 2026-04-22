package com.tang.kite.sql.dialect

import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.ROWS_FETCH_NEXT
import com.tang.kite.constants.SqlString.ROWS_ONLY
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * @author Tang
 */
class DerbyDialect : SqlDialect {

    override fun getType(): DatabaseType {
        return DatabaseType.DERBY
    }

    override fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause) {
        parameters.add((limitClause.pageNumber - 1) * limitClause.pageSize)
        parameters.add(limitClause.pageSize)
        sql.append("$OFFSET$QUESTION_MARK$ROWS_FETCH_NEXT$QUESTION_MARK$ROWS_ONLY")
    }

}
