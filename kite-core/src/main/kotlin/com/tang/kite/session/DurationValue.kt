package com.tang.kite.session

import com.tang.kite.executor.ExecutionResult

/**
 * Duration value container for tracking SQL execution time breakdowns.
 * Records prepare time, execution time, mapping time, and total elapsed time.
 *
 * @author Tang
 */
class DurationValue {

    val prepareTime: Long

    val executionTime: Long

    val mappingTime: Long

    val elapsedTime: Long

    constructor(prepareTime: Long, executionTime: Long, mappingTime: Long, elapsedTime: Long) {
        this.prepareTime = prepareTime
        this.executionTime = executionTime
        this.mappingTime = mappingTime
        this.elapsedTime = elapsedTime
    }

    constructor(prepareTime: Long, executionTime: Long, elapsedTime: Long) : this(prepareTime, executionTime, 0L, elapsedTime)

    constructor(executionResult: ExecutionResult<*>, prepareTime: Long, elapsedTime: Long) : this(prepareTime, executionResult.executionTime, executionResult.mappingTime, elapsedTime)

}
