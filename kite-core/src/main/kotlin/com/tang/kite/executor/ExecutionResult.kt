package com.tang.kite.executor

import com.tang.kite.session.DurationValue

/**
 * @author Tang
 */
class ExecutionResult<T> {

    val data: T

    val executionTime: Long

    val mappingTime: Long

    constructor(data: T, executionTime: Long, mappingTime: Long) {
        this.data = data
        this.executionTime = executionTime
        this.mappingTime = mappingTime
    }

    constructor(data: T, executionTime: Long) : this(data, executionTime, 0L)

    fun toValue(prepareTime: Long, elapsedTime: Long): DurationValue {
        return DurationValue(this, prepareTime, elapsedTime)
    }

}
