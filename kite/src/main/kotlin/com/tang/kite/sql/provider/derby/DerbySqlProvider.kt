package com.tang.kite.sql.provider.derby

import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.ROWS_FETCH_NEXT
import com.tang.kite.constants.SqlString.ROWS_ONLY
import com.tang.kite.sql.provider.AbstractSqlProvider
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * Derby SQL provider
 *
 * @author Tang
 */
class DerbySqlProvider : AbstractSqlProvider() {

    override fun providerType(): DatabaseType {
        return DatabaseType.DERBY
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
        return "$OFFSET$QUESTION_MARK$ROWS_FETCH_NEXT$QUESTION_MARK$ROWS_ONLY"
    }

}
