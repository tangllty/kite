package com.tang.kite.sql.dialect

import com.tang.kite.constants.SqlString.OFFSET
import com.tang.kite.constants.SqlString.QUESTION_MARK
import com.tang.kite.constants.SqlString.ROWS_FETCH_NEXT
import com.tang.kite.constants.SqlString.ROWS_ONLY
import com.tang.kite.sql.LimitClause
import com.tang.kite.sql.enumeration.DatabaseType

/**
 * Abstract base class for SQL dialects
 * Provides default implementations that can be overridden by specific dialects
 *
 * @author Tang
 */
abstract class AbstractSqlDialect(private val databaseType: DatabaseType) : SqlDialect {

    override fun getType(): DatabaseType {
        return databaseType
    }

    override fun applyLimitClause(sql: StringBuilder, parameters: MutableList<Any?>, limitClause: LimitClause) {
        parameters.add((limitClause.pageNumber - 1) * limitClause.pageSize)
        parameters.add(limitClause.pageSize)
        sql.append("$OFFSET$QUESTION_MARK$ROWS_FETCH_NEXT$QUESTION_MARK$ROWS_ONLY")
    }

    override fun supportsCascade(): Boolean = true

    override fun supportsCommentOnTable(): Boolean = true

    override fun supportsCommentOnColumn(): Boolean = true

    override fun requiresTableForDropIndex(): Boolean = false

    override fun getAutoIncrementKeyword(): String = "generated as identity"

    override fun getAddColumnKeyword(): String = "add column"

    override fun getDropColumnKeyword(): String = "drop column"

    override fun getAlterColumnKeyword(): String = "alter column"

}
