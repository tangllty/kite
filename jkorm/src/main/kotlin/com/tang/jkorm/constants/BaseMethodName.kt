package com.tang.jkorm.constants

/**
 * @author Tang
 */
object BaseMethodName {

    const val INSERT = "insert"

    const val INSERT_SELECTIVE = "insertSelective"

    const val UPDATE = "update"

    const val UPDATE_SELECTIVE = "updateSelective"

    const val DELETE = "delete"

    const val DELETE_BY_ID = "deleteById"

    const val SELECT = "select"

    const val SELECT_BY_ID = "selectById"

    fun isBaseMethod(methodName: String): Boolean {
        return when (methodName) {
            INSERT, INSERT_SELECTIVE, UPDATE, UPDATE_SELECTIVE, DELETE, DELETE_BY_ID, SELECT, SELECT_BY_ID  -> true
            else -> false
        }
    }

}
