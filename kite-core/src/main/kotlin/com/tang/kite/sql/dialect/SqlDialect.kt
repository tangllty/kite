package com.tang.kite.sql.dialect

import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * @author Tang
 */
interface SqlDialect {

    fun getType(): DatabaseType

    fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause)

    fun supportsCascade(): Boolean

    fun supportsCommentOnTable(): Boolean

    fun supportsCommentOnColumn(): Boolean

    fun requiresTableForDropIndex(): Boolean

    fun getAutoIncrementKeyword(): String

    fun getAddColumnKeyword(): String

    fun getDropColumnKeyword(): String

    fun getAlterColumnKeyword(): String

}
