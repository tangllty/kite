package com.tang.kite.exception

/**
 * Exception thrown when optimistic locking fails
 *
 * This exception is thrown when an update operation fails due to version mismatch,
 * indicating that the data has been modified by another transaction.
 *
 * @author Tang
 */
class OptimisticLockException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {

    constructor(message: String?) : this(message, null)

    constructor(cause: Throwable?) : this(null, cause)

}
