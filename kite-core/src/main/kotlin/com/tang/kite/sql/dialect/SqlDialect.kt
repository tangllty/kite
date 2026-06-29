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

    /**
     * Get keyword between column name and data type when altering column type
     */
    fun getAlterColumnTypeKeyword(): String

    /**
     * Whether to split modify type and null constraint into two separate SQL
     */
    fun needSplitAlterTypeAndNull(): Boolean

}
