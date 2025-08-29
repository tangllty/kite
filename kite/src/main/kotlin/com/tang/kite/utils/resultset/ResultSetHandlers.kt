package com.tang.kite.utils.resultset

import com.tang.kite.constants.SqlString
import com.tang.kite.utils.Reflects
import com.tang.kite.utils.Reflects.getField
import com.tang.kite.utils.Reflects.setResultValue
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
import java.sql.JDBCType
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * @author Tang
 */
object ResultSetHandlers {

    fun <T> getList(resultSet: ResultSet, type: Class<T>): List<T> {
        val list = mutableListOf<T>()
        while (resultSet.next()) {
            val entity = getEntity(resultSet, type)
            list.add(entity)
        }
        resultSet.close()
        return list
    }

    private fun <T> getEntity(resultSet: ResultSet, type: Class<T>): T {
        val metaData = resultSet.metaData
        val metaDataMap = mutableMapOf<String, MutableList<MetaData>>()
        val entity = type.getDeclaredConstructor().newInstance()
        val tableName = Reflects.getTableName(type)

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

        // Handle fields with table name (main table and sub tables)
        val metaDataList = metaDataMap[tableName] ?: emptyList()
        metaDataList.forEach {
            val field = getField(type, it.columnName)
            if (field != null) {
                setResultValue(field, entity, it.columnValue)
            }
        }

        // Handle fields of joined tables
        val joins = Reflects.getJoins(type)
        val joinEntities = mutableMapOf<String, Any>()
        joins.forEach { join ->
            val joinTableName = Reflects.getTableName(join.type)
            val joinEntity = join.type.getDeclaredConstructor().newInstance()
            joinEntities[joinTableName] = joinEntity
            val joinMetaData = metaDataMap[joinTableName] ?: return@forEach
            joinMetaData.forEach {
                val field = getField(join.type, it.columnName) ?: getField(join.type, toCamelCase(it.columnName))
                if (field != null) {
                    setResultValue(field, joinEntity, it.columnValue)
                }
            }
            setResultValue(join, entity, joinEntity)
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
