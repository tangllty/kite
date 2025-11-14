package com.tang.kite.sql.provider.h2

import com.tang.kite.constants.SqlString.LIMIT
import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * @author Tang
 */
class H2SqlProvider : AbstractSqlProvider() {

    override fun providerType(): DatabaseType {
        return DatabaseType.H2
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add(pageSize)
        parameters.add((pageNumber - 1) * pageSize)
        return "$LIMIT$QUESTION_MARK$OFFSET$QUESTION_MARK"
    }

}
