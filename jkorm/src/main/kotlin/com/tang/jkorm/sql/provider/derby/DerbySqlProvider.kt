package com.tang.jkorm.sql.provider.derby

import com.tang.jkorm.constants.SqlString.OFFSET
import com.tang.jkorm.constants.SqlString.ROWS_FETCH_NEXT
import com.tang.jkorm.constants.SqlString.ROWS_ONLY
import com.tang.jkorm.sql.provider.AbstractSqlProvider

/**
 * Derby SQL provider
 *
 * @author Tang
 */
class DerbySqlProvider : AbstractSqlProvider() {

    override fun appendLimit(sql: StringBuilder, pageNumber: Long, pageSize: Long) {
        sql.append(OFFSET).append((pageNumber - 1) * pageSize).append(ROWS_FETCH_NEXT).append(pageSize).append(ROWS_ONLY)
    }

}
