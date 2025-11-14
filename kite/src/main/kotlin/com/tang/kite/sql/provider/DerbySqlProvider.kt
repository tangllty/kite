package com.tang.kite.sql.provider

import com.tang.kite.constants.SqlString
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * Derby SQL provider
 *
 * @author Tang
 */
@Deprecated("Remove in future versions")
class DerbySqlProvider : AbstractSqlProvider() {

    override fun providerType(): DatabaseType {
        return DatabaseType.DERBY
    }

    override fun getLimit(parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long): String {
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
        return "${SqlString.OFFSET}${SqlString.QUESTION_MARK}${SqlString.ROWS_FETCH_NEXT}${SqlString.QUESTION_MARK}${SqlString.ROWS_ONLY}"
    }

}
