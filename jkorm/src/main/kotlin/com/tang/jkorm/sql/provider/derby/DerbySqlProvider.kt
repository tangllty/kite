package com.tang.jkorm.sql.provider.derby

import com.tang.jkorm.constants.SqlString.OFFSET
import com.tang.jkorm.constants.SqlString.QUESTION_MARK
import com.tang.jkorm.constants.SqlString.ROWS_FETCH_NEXT
import com.tang.jkorm.constants.SqlString.ROWS_ONLY
import com.tang.jkorm.sql.provider.AbstractSqlProvider
import com.tang.jkorm.sql.provider.ProviderType

/**
 * Derby SQL provider
 *
 * @author Tang
 */
class DerbySqlProvider : AbstractSqlProvider() {

    override fun providerType(): ProviderType {
        return ProviderType.DERBY
    }

    override fun appendLimit(sql: StringBuilder, parameters: MutableList<Any?>, pageNumber: Long, pageSize: Long) {
        sql.append(OFFSET).append(QUESTION_MARK).append(ROWS_FETCH_NEXT).append(QUESTION_MARK).append(ROWS_ONLY)
        parameters.add((pageNumber - 1) * pageSize)
        parameters.add(pageSize)
    }

}
