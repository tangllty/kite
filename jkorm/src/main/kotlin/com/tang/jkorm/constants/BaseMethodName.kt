package com.tang.jkorm.constants

import java.lang.reflect.Method

/**
 * @author Tang
 */
object BaseMethodName {

    private const val INSERT = "insert"

    fun isInsert(method: Method): Boolean {
        return method.name == INSERT && method.parameterCount == 1 && method.parameterTypes[0] is Any
    }

    private const val INSERT_SELECTIVE = "insertSelective"

    fun isInsertSelective(method: Method): Boolean {
        return method.name == INSERT && method.parameterCount == 1 && method.parameterTypes[0] is Any
    }

    private const val UPDATE = "update"

    fun isUpdate(method: Method): Boolean {
        return method.name == UPDATE && method.parameterCount == 1 && method.parameterTypes[0] is Any
    }

    private const val UPDATE_SELECTIVE = "updateSelective"

    fun isUpdateSelective(method: Method): Boolean {
        return method.name == UPDATE_SELECTIVE && method.parameterCount == 1 && method.parameterTypes[0] is Any
    }

    private const val DELETE = "delete"

    fun isDelete(method: Method): Boolean {
        return method.name == DELETE && method.parameterCount == 1 && method.parameterTypes[0] is Any
    }

    private const val DELETE_BY_ID = "deleteById"

    fun isDeleteById(method: Method): Boolean {
        return method.name == DELETE_BY_ID && method.parameterCount == 1 && method.parameterTypes[0].name == "long"
    }

    private const val SELECT = "select"

    fun isSelect(method: Method): Boolean {
        return method.name == SELECT
    }

    private const val SELECT_BY_ID = "selectById"

    fun isSelectById(method: Method): Boolean {
        return method.name == SELECT_BY_ID && method.parameterCount == 1 && method.parameterTypes[0].name == "long"
    }

    private const val COUNT = "count"

    fun isCount(method: Method): Boolean {
        return method.name == COUNT
    }

    fun isBaseMethod(methodName: String): Boolean {
        return when (methodName) {
            INSERT, INSERT_SELECTIVE, UPDATE, UPDATE_SELECTIVE, DELETE, DELETE_BY_ID, SELECT, SELECT_BY_ID  -> true
            else -> false
        }
    }

}
