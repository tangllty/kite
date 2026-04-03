package com.tang.kite.session.defaults

import com.tang.kite.executor.ExecutionResult
import com.tang.kite.session.DurationValue
import com.tang.kite.sql.statement.BatchSqlStatement

/**
 * @author Tang
 */
class BatchSqlStatementValue<T>(val batchSqlStatement: BatchSqlStatement, val result: ExecutionResult<T>, val duration: DurationValue)
