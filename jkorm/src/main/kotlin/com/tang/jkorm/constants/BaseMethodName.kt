package com.tang.jkorm.constants

import com.tang.jkorm.paginate.OrderItem
import java.lang.reflect.Method

/**
 * @author Tang
 */
object BaseMethodName {

    private fun Method.countIsZero(): Boolean {
        return parameterCount == 0
    }

    private fun Method.countIsOne(): Boolean {
        return parameterCount == 1
    }

    private fun Method.countIsTwo(): Boolean {
        return parameterCount == 2
    }

    private fun Method.firstParameterIsAny(): Boolean {
        return isAny(parameterTypes[0])
        return parameterTypes[0] is Any
    }

    private fun Method.firstParameterIsIterable(): Boolean {
        return Iterable::class.java.isAssignableFrom(parameterTypes[0])
    }

    private fun Method.firstParameterIsLong(): Boolean {
        return parameterTypes[0].name == Long::class.java.name
    }

    private fun Method.secondParameterIsAny(): Boolean {
        return isAny(parameterTypes[1])
    }

    private fun Method.secondParameterIsLong(): Boolean {
        return parameterTypes[1].name == Long::class.java.name
    }

    private fun Method.thirdParameterIsAny(): Boolean {
        return isAny(parameterTypes[2])
    }

    private fun isAny(type: Class<*>): Boolean {
        return type == Any::class.java
    }

    private fun isOrderItem(type: Class<*>): Boolean {
        return type == OrderItem::class.java
    }

    private const val INSERT = "insert"

    fun isInsert(method: Method): Boolean {
        return method.name == INSERT && method.countIsOne() && method.firstParameterIsAny()
    }

    private const val INSERT_SELECTIVE = "insertSelective"

    fun isInsertSelective(method: Method): Boolean {
        return method.name == INSERT_SELECTIVE && method.countIsOne() && method.firstParameterIsAny()
    }

    private const val BATCH_INSERT = "batchInsert"

    fun isBatchInsert(method: Method): Boolean {
        return method.name == BATCH_INSERT && method.countIsOne() && method.firstParameterIsIterable()
    }

    private const val BATCH_INSERT_SELECTIVE = "batchInsertSelective"

    fun isBatchInsertSelective(method: Method): Boolean {
        return method.name == BATCH_INSERT_SELECTIVE && method.countIsOne() && method.firstParameterIsIterable()
    }

    private const val UPDATE = "update"

    fun isUpdate(method: Method): Boolean {
        return method.name == UPDATE && method.countIsOne() && method.firstParameterIsAny()
    }

    fun isUpdateCondition(method: Method): Boolean {
        return method.name == UPDATE && method.countIsTwo() && method.firstParameterIsAny() && method.secondParameterIsAny()
    }

    private const val UPDATE_SELECTIVE = "updateSelective"

    fun isUpdateSelective(method: Method): Boolean {
        return method.name == UPDATE_SELECTIVE && method.countIsOne() && method.firstParameterIsAny()
    }

    private const val DELETE = "delete"

    fun isDelete(method: Method): Boolean {
        return method.name == DELETE && method.countIsOne() && method.firstParameterIsAny()
    }

    private const val DELETE_BY_ID = "deleteById"

    fun isDeleteById(method: Method): Boolean {
        return method.name == DELETE_BY_ID && method.countIsOne() && method.firstParameterIsLong()
    }

    private const val SELECT = "select"

    fun isSelect(method: Method): Boolean {
        if (method.name != SELECT) return false
        if (method.countIsZero()) return true
        if (method.countIsOne()) {
            if (method.firstParameterIsAny()) return true
            if (isOrderItem(method.parameterTypes[0].componentType)) return true
        }
        if (method.countIsTwo() && method.firstParameterIsAny() && isOrderItem(method.parameterTypes[1].componentType)) return true
        return false
    }

    private const val SELECT_BY_ID = "selectById"

    fun isSelectById(method: Method): Boolean {
        return method.name == SELECT_BY_ID && method.countIsOne() && method.firstParameterIsLong()
    }

    private const val COUNT = "count"

    fun isCount(method: Method): Boolean {
        return method.name ==  COUNT && (method.countIsZero() || method.countIsOne() && method.firstParameterIsAny())
    }

    private const val PAGINATE = "paginate"

    fun isPaginate(method: Method): Boolean {
        if (method.name != PAGINATE) return false
        if (!method.firstParameterIsLong() || !method.secondParameterIsLong()) return false
        if (method.countIsTwo()) return true
        if (method.parameterCount == 3) {
            if (method.thirdParameterIsAny()) return true
            if (isOrderItem(method.parameterTypes[2].componentType)) return true
        }
        if (method.parameterCount == 4 && isAny(method.parameterTypes[2])
            && isOrderItem(method.parameterTypes[3].componentType)) return true
        return false
    }

    fun isBaseMethod(methodName: String): Boolean {
        return when (methodName) {
            INSERT, INSERT_SELECTIVE, BATCH_INSERT, BATCH_INSERT_SELECTIVE,
            UPDATE, UPDATE_SELECTIVE,
            DELETE, DELETE_BY_ID,
            SELECT, SELECT_BY_ID,
            COUNT,
            PAGINATE -> true
            else -> false
        }
    }

}
