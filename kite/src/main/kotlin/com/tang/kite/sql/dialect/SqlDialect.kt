package com.tang.kite.sql.dialect

import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.provider.ProviderType

/**
 * @author Tang
 */
interface SqlDialect {

    fun getType(): ProviderType

    fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause)

}
