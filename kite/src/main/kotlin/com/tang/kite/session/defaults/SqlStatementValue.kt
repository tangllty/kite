package com.tang.kite.session.defaults

import com.tang.kite.executor.ExecutionResult
import com.tang.kite.session.DurationValue
import com.tang.kite.sql.statement.SqlStatement

/**
 * @author Tang
 */
class SqlStatementValue<T>(val sqlStatement: SqlStatement, val result: ExecutionResult<T>, val duration: DurationValue)
