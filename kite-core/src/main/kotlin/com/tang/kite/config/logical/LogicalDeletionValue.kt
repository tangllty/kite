package com.tang.kite.config.logical

/**
 * Data class representing the logical deletion values for normal and deleted states.
 * Stores the value used when a record is in normal state and the value used when it is logically deleted.
 *
 * @author Tang
 */
data class LogicalDeletionValue(

    val normalValue: Any,

    val deletedValue: Any

)
