package com.tang.kite.constants

import com.tang.kite.paginate.OrderItem
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.query.QueryWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import java.io.Serializable
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
    }

    private fun Method.firstParameterIsIterable(): Boolean {
        return Iterable::class.java.isAssignableFrom(parameterTypes[0])
    }

    private fun Method.firstParameterIsLong(): Boolean {
        return parameterTypes[0].name == Long::class.java.name
    }

    private fun Method.firstParameterIsSerializable(): Boolean {
        return parameterTypes[0].name == Serializable::class.java.name
    }

    private fun Method.secondParameterIsAny(): Boolean {
        return isAny(parameterTypes[1])
    }

    private fun Method.secondParameterIsLong(): Boolean {
        return parameterTypes[1].name == Long::class.java.name
    }

    private fun Method.secondParameterIsInt(): Boolean {
        return parameterTypes[1].name == Int::class.java.name
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

    private const val INSERT_VALUES = "insertValues"

    fun isInsertValues(method: Method): Boolean {
        return method.name == INSERT_VALUES && method.countIsTwo() && method.firstParameterIsIterable() && method.secondParameterIsInt()
    }

    private const val BATCH_INSERT = "batchInsert"

    fun isBatchInsert(method: Method): Boolean {
        return method.name == BATCH_INSERT && method.countIsTwo() && method.firstParameterIsIterable() && method.secondParameterIsInt()
    }

    private const val BATCH_INSERT_SELECTIVE = "batchInsertSelective"

    fun isBatchInsertSelective(method: Method): Boolean {
        return method.name == BATCH_INSERT_SELECTIVE && method.countIsTwo() && method.firstParameterIsIterable() && method.secondParameterIsInt()
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

    private const val UPDATE_WRAPPER = "updateWrapper"

    fun isUpdateWrapper(method: Method): Boolean {
        return method.name == UPDATE_WRAPPER && method.countIsOne() && method.parameterTypes[0].kotlin == UpdateWrapper::class
    }

    private const val BATCH_UPDATE = "batchUpdate"

    fun isBatchUpdate(method: Method): Boolean {
        return method.name == BATCH_UPDATE && method.countIsTwo() && method.firstParameterIsIterable() && method.secondParameterIsInt()
    }

    private const val BATCH_UPDATE_SELECTIVE = "batchUpdateSelective"

    fun isBatchUpdateSelective(method: Method): Boolean {
        return method.name == BATCH_UPDATE_SELECTIVE && method.countIsTwo() && method.firstParameterIsIterable() && method.secondParameterIsInt()
    }

    private const val DELETE = "delete"

    fun isDelete(method: Method): Boolean {
        return method.name == DELETE && method.countIsOne() && method.firstParameterIsAny()
    }

    private const val DELETE_BY_ID = "deleteById"

    fun isDeleteById(method: Method): Boolean {
        return method.name == DELETE_BY_ID && method.countIsOne() && method.firstParameterIsSerializable()
    }

    private const val DELETE_BY_IDS = "deleteByIds"

    fun isDeleteByIds(method: Method): Boolean {
        return method.name == DELETE_BY_IDS && method.countIsOne() && method.firstParameterIsIterable()
    }

    private const val DELETE_WRAPPER = "deleteWrapper"

    fun isDeleteWrapper(method: Method): Boolean {
        return method.name == DELETE_WRAPPER && method.countIsOne() && method.parameterTypes[0].kotlin == DeleteWrapper::class
    }

    private const val SELECT = "select"

    private fun isSelectParameter(method: Method): Boolean {
        if (method.countIsZero()) return true
        if (method.countIsOne()) {
            if (method.firstParameterIsAny()) return true
            if (isOrderItem(method.parameterTypes[0].componentType)) return true
        }
        if (method.countIsTwo() && method.firstParameterIsAny() && isOrderItem(method.parameterTypes[1].componentType)) return true
        return false
    }

    fun isSelect(method: Method): Boolean {
        if (method.name != SELECT) return false
        return isSelectParameter(method)
    }

    private const val QUERY_WRAPPER = "queryWrapper"

    fun isQueryWrapper(method: Method): Boolean {
        return method.name == QUERY_WRAPPER && method.countIsOne() && method.parameterTypes[0].kotlin == QueryWrapper::class
    }

    private const val SELECT_BY_ID = "selectById"

    fun isSelectById(method: Method): Boolean {
        return method.name == SELECT_BY_ID && method.countIsOne() && method.firstParameterIsSerializable()
    }

    private const val SELECT_ONE_WRAPPER = "selectOneWrapper"

    fun isSelectOneWrapper(method: Method): Boolean {
        return method.name == SELECT_ONE_WRAPPER && method.countIsOne() && method.parameterTypes[0].kotlin == QueryWrapper::class
    }

    private const val SELECT_WITH_JOINS = "selectWithJoins"

    fun isSelectWithJoins(method: Method): Boolean {
        if (method.name != SELECT_WITH_JOINS) return false
        return isSelectParameter(method)
    }

    private  const val SELECT_BY_ID_WITH_JOINS = "selectByIdWithJoins"

    fun isSelectByIdWithJoins(method: Method): Boolean {
        return method.name == SELECT_BY_ID_WITH_JOINS && method.countIsOne() && method.firstParameterIsSerializable()
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

    fun isBaseMethod(method: Method): Boolean {
        val methodName = method.name
        val isBaseMethodName = when (methodName) {
            INSERT, INSERT_SELECTIVE, INSERT_VALUES, BATCH_INSERT, BATCH_INSERT_SELECTIVE,
            UPDATE, UPDATE_SELECTIVE, UPDATE_WRAPPER, BATCH_UPDATE, BATCH_UPDATE_SELECTIVE,
            DELETE, DELETE_BY_ID, DELETE_BY_IDS, DELETE_WRAPPER,
            SELECT, SELECT_BY_ID,
            QUERY_WRAPPER, SELECT_ONE_WRAPPER,
            SELECT_WITH_JOINS, SELECT_BY_ID_WITH_JOINS,
            COUNT,
            PAGINATE -> true
            else -> false
        }
        if (!isBaseMethodName) return false
        return when (methodName) {
            INSERT -> isInsert(method)
            INSERT_SELECTIVE -> isInsertSelective(method)
            INSERT_VALUES -> isInsertValues(method)
            BATCH_INSERT -> isBatchInsert(method)
            BATCH_INSERT_SELECTIVE -> isBatchInsertSelective(method)
            UPDATE -> isUpdate(method) || isUpdateCondition(method)
            UPDATE_SELECTIVE -> isUpdateSelective(method)
            UPDATE_WRAPPER -> isUpdateWrapper(method)
            BATCH_UPDATE -> isBatchUpdate(method)
            BATCH_UPDATE_SELECTIVE -> isBatchUpdateSelective(method)
            DELETE -> isDelete(method)
            DELETE_BY_ID -> isDeleteById(method)
            DELETE_BY_IDS -> isDeleteByIds(method)
            DELETE_WRAPPER -> isDeleteWrapper(method)
            SELECT -> isSelect(method)
            QUERY_WRAPPER -> isQueryWrapper(method)
            SELECT_BY_ID -> isSelectById(method)
            SELECT_ONE_WRAPPER -> isSelectOneWrapper(method)
            SELECT_WITH_JOINS -> isSelectWithJoins(method)
            SELECT_BY_ID_WITH_JOINS -> isSelectByIdWithJoins(method)
            COUNT -> isCount(method)
            PAGINATE -> isPaginate(method)
            else -> false
        }

    }

}
