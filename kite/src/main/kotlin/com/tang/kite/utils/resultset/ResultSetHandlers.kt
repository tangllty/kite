package com.tang.kite.utils.resultset

import com.tang.kite.constants.SqlString
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getField
import com.tang.kite.utils.Reflects.setResultValue
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.sql.JDBCType
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * @author Tang
 */
object ResultSetHandlers {

    fun <T> getList(resultSet: ResultSet, type: Class<T>): List<T> {
        val list = mutableListOf<T>()
        val metaDataList: MutableList<MutableMap<String, MutableList<MetaData>>> = mutableListOf()
        val mainTableName = Reflects.getTableName(type)

        while (resultSet.next()) {
            val metaData = resultSet.metaData
            val metaDataMap = mutableMapOf<String, MutableList<MetaData>>()

            for (i in 1..metaData.columnCount) {
                val columnTableName = metaData.getTableName(i).lowercase()
                val className = metaData.getColumnClassName(i)
                val jdbcType = JDBCType.valueOf(metaData.getColumnType(i))
                val columnName = metaData.getColumnName(i)
                val columnValue = resultSet.getObject(i)

                // If there is no table name, use special key "__NO_TABLE__"
                val effectiveTableName = columnTableName.ifBlank { "__NO_TABLE__" }
                metaDataMap.computeIfAbsent(effectiveTableName) { mutableListOf() }
                    .add(MetaData(className, jdbcType, columnName, columnValue))
            }
            metaDataList.add(metaDataMap)

            // If the main table data is the same as the previous row, merge the sub-table data
            if (metaDataMap[mainTableName] != null && metaDataList.size > 1) {
                val previousMetaDataMap = metaDataList[metaDataList.size - 2]
                val previousMainTableData = previousMetaDataMap[mainTableName]
                val currentMainTableData = metaDataMap[mainTableName]
                if (previousMainTableData != null && currentMainTableData != null &&
                    previousMainTableData.all { prev ->
                        currentMainTableData.any { curr ->
                            prev.columnName == curr.columnName && prev.columnValue == curr.columnValue
                        }
                    }
                ) {
                    // Merge sub-table data into the previous row
                    metaDataMap.forEach { (tableName, metaList) ->
                        if (tableName != mainTableName) {
                            val previousList = previousMetaDataMap.computeIfAbsent(tableName) { mutableListOf() }
                            previousList.addAll(metaList)
                        }
                    }
                    // Remove the current row as it's merged
                    metaDataMap.remove(mainTableName)
                    metaDataList.removeAt(metaDataList.size - 1)
                }
            }
        }

        metaDataList.forEach {
            val entity = getEntity(it, type)
            list.add(entity)
        }

        resultSet.close()
        return list
    }

    private fun <T> getEntity(metaDataMap: MutableMap<String, MutableList<MetaData>>, type: Class<T>): T {
        val entity = type.getDeclaredConstructor().newInstance()
        val tableName = Reflects.getTableName(type)

        // Handle fields with table name (main table and sub tables)
        val metaDataList = metaDataMap[tableName] ?: emptyList()
        metaDataList.forEach {
            val field = getField(type, it.columnName)
            if (field != null) {
                setResultValue(field, entity, it.columnValue)
            }
        }

        // Handle fields of joined tables
        val joins = Reflects.getAllJoins(type)
        val joinEntities = mutableMapOf<String, Any>()
        joins.forEach { join ->
            if (Iterable::class.java.isAssignableFrom(join.type) || join.type.isArray) {
                handleJoinList(join, metaDataMap, entity)
            } else {
                handleJoinEntity(join, joinEntities, metaDataMap, entity)
            }
        }

        // Handle fields without table name, assign to main table first, then to sub table if not found
        val noTableMetaDataList = metaDataMap["__NO_TABLE__"] ?: emptyList()
        noTableMetaDataList.forEach {
            val mainField = getField(type, it.columnName) ?: getField(type, toCamelCase(it.columnName))
            if (mainField != null) {
                setResultValue(mainField, entity, it.columnValue)
                return@forEach
            }
            for (join in joins) {
                val joinEntity = joinEntities[Reflects.getTableName(join.type)] ?: continue
                val joinField = getField(join.type, it.columnName) ?: getField(join.type, toCamelCase(it.columnName))
                if (joinField != null) {
                    setResultValue(joinField, joinEntity, it.columnValue)
                    break
                }
            }
        }
        return entity
    }

    private fun <T> handleJoinList(join: Field, metaDataMap: MutableMap<String, MutableList<MetaData>>, entity: T) {
        val type = join.genericType as ParameterizedType
        val joinGenericType = type.actualTypeArguments[0] as Class<*>
        val joinTableName = Reflects.getTableName(joinGenericType)
        val list = mutableListOf<Any>()
        val joinMetaData = metaDataMap[joinTableName] ?: return
        var firstColumnName: String? = null
        var joinEntity: Any? = null
        joinMetaData.forEach {
            if (firstColumnName == null) {
                firstColumnName = it.columnName
                joinEntity = joinGenericType.getDeclaredConstructor().newInstance()
            } else if (firstColumnName == it.columnName) {
                if (joinEntity != null) {
                    list.add(joinEntity)
                }
                joinEntity = joinGenericType.getDeclaredConstructor().newInstance()
            }
            val field =
                getField(joinGenericType, it.columnName) ?: getField(joinGenericType, toCamelCase(it.columnName))
            if (field != null && joinEntity != null) {
                setResultValue(field, joinEntity, it.columnValue)
            }
        }
        if (joinEntity != null) {
            list.add(joinEntity)
        }

        if (Iterable::class.java.isAssignableFrom(join.type)) {
            setResultValue(join, entity, list)
        } else if (join.type.isArray) {
            val array = Array.newInstance(joinGenericType, list.size)
            list.forEachIndexed { index, any ->
                Array.set(array, index, any)
            }
            setResultValue(join, entity, array)
        }
    }

    private fun <T> handleJoinEntity(join: Field, joinEntities: MutableMap<String, Any>, metaDataMap: MutableMap<String, MutableList<MetaData>>, entity: T) {
        val joinTableName = Reflects.getTableName(join.type)
        val joinEntity = join.type.getDeclaredConstructor().newInstance()
        joinEntities[joinTableName] = joinEntity
        val joinMetaData = metaDataMap[joinTableName] ?: return
        joinMetaData.forEach {
            val field = getField(join.type, it.columnName) ?: getField(join.type, toCamelCase(it.columnName))
            if (field != null) {
                setResultValue(field, joinEntity, it.columnValue)
            }
        }
        setResultValue(join, entity, joinEntity)
    }

    /**
     * Convert snake_case to camelCase.
     */
    private fun toCamelCase(columnName: String): String {
        return columnName.split("_")
            .joinToString("") {
                it.lowercase().replaceFirstChar {
                    char -> char.uppercase()
                }
            }.replaceFirstChar { it.lowercase() }
    }

    fun getCount(resultSet: ResultSet): Long {
        resultSet.next()
        val count = resultSet.getLong(1)
        resultSet.close()
        return count
    }

    fun setGeneratedKey(sql: String, preparedStatement: PreparedStatement, parameters: Any) {
        if (hasGeneratedKey(sql, parameters).not()) {
            return
        }
        val resultSet = preparedStatement.generatedKeys
        if (parameters.javaClass.name == ArrayList::class.java.name) {
            val list = parameters as List<*>
            list.forEach {
                setIdValue(resultSet, it!!)
            }
            resultSet.close()
            return
        }
        setIdValue(resultSet, parameters)
        resultSet.close()
    }

    fun hasGeneratedKey(sql: String, parameters: Any): Boolean {
        val excludedTypes = listOf(UpdateWrapper::class.java.name, DeleteWrapper::class.java.name, String::class.java.name)
        if (parameters.javaClass.name in excludedTypes) {
            return false
        }
        if (Number::class.java.isAssignableFrom(parameters::class.java)) {
            return false
        }
        if (Iterable::class.java.isAssignableFrom(parameters::class.java)) {
            return false
        }
        var param = parameters
        if (parameters.javaClass.name == ArrayList::class.java.name) {
            val list = parameters as List<*>
            if (list.isNotEmpty()) {
                param = list.first()!!
            }
        }
        val idField = Reflects.getIdField(param.javaClass)
        val autoIncrementId = Reflects.isAutoIncrementId(idField)
        return sql.startsWith(SqlString.INSERT, true) && autoIncrementId
    }

    private fun setIdValue(resultSet: ResultSet, it: Any) {
        val next = resultSet.next()
        if (next.not()) {
            return
        }
        val generatedKey = resultSet.getLong(1)
        val idField = Reflects.getIdField(it.javaClass)
        setResultValue(idField, it, generatedKey)
    }

}
