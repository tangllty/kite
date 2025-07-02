package com.tang.kite.utils

import com.tang.kite.constants.SqlString.INSERT
import com.tang.kite.sql.SqlStatement
import com.tang.kite.wrapper.delete.DeleteWrapper
import com.tang.kite.wrapper.update.UpdateWrapper
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
        return list
    }

    private fun <T> getEntity(resultSet: ResultSet, type: Class<T>): T {
        val metaData = resultSet.metaData
        val metaDataMap = mutableMapOf<String, MutableList<Pair<String, Any?>>>()
        val entity = type.getDeclaredConstructor().newInstance()
        val tableName = Reflects.getTableName(type)

        for (i in 1..metaData.columnCount) {
            val columnTableName = metaData.getTableName(i).lowercase()
            val columnName = metaData.getColumnName(i)
            val columnValue = resultSet.getObject(i)

            // If there is no table name, use special key "__NO_TABLE__"
            val effectiveTableName = if (columnTableName.isBlank()) "__NO_TABLE__" else columnTableName
            metaDataMap.computeIfAbsent(effectiveTableName) { mutableListOf() }
                .add(Pair(columnName, columnValue))
        }

        // Handle fields with table name (main table and sub tables)
        val metaDataList = metaDataMap[tableName] ?: emptyList()
        metaDataList.forEach { (columnName, columnValue) ->
            val field = Reflects.getField(type, columnName)
            if (field != null) {
                Reflects.makeAccessible(field, entity as Any)
                field.set(entity, columnValue)
            }
        }

        // Handle fields of joined tables
        val joins = Reflects.getJoins(type)
        val joinEntities = mutableMapOf<String, Any>()
        joins.forEach {
            val joinTableName = Reflects.getTableName(it.type)
            val joinEntity = it.type.getDeclaredConstructor().newInstance()
            joinEntities[joinTableName] = joinEntity
            val joinMetaData = metaDataMap[joinTableName] ?: return@forEach
            joinMetaData.forEach { (columnName, columnValue) ->
                val field = Reflects.getField(it.type, columnName) ?: Reflects.getField(it.type, toCamelCase(columnName))
                if (field != null) {
                    Reflects.makeAccessible(field, joinEntity as Any)
                    field.set(joinEntity, columnValue)
                }
            }
            Reflects.makeAccessible(it, entity as Any)
            it.set(entity, joinEntity)
        }

        // Handle fields without table name, assign to main table first, then to sub table if not found
        val noTableMetaDataList = metaDataMap["__NO_TABLE__"] ?: emptyList()
        noTableMetaDataList.forEach { (columnName, columnValue) ->
            val mainField = Reflects.getField(type, columnName) ?: Reflects.getField(type, toCamelCase(columnName))
            if (mainField != null) {
                Reflects.makeAccessible(mainField, entity as Any)
                mainField.set(entity, columnValue)
                return@forEach
            }
            for (join in joins) {
                val joinEntity = joinEntities[Reflects.getTableName(join.type)] ?: continue
                val joinField = Reflects.getField(join.type, columnName) ?: Reflects.getField(join.type, toCamelCase(columnName))
                if (joinField != null) {
                    Reflects.makeAccessible(joinField, joinEntity)
                    joinField.set(joinEntity, columnValue)
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
        return resultSet.getLong(1)
    }

    fun setGeneratedKey(statement: SqlStatement, preparedStatement: PreparedStatement, parameter: Any) {
        if (hasGeneratedKey(statement, parameter).not()) {
            return
        }
        val resultSet = preparedStatement.generatedKeys
        if (parameter.javaClass.name == ArrayList::class.java.name) {
            val list = parameter as List<*>
            list.forEach {
                setIdValue(resultSet, it!!)
            }
            return
        }
        setIdValue(resultSet, parameter)
    }

    fun hasGeneratedKey(statement: SqlStatement, parameter: Any): Boolean {
        val excludedTypes = listOf(UpdateWrapper::class.java.name, DeleteWrapper::class.java.name, String::class.java.name)
        if (parameter.javaClass.name in excludedTypes) {
            return false
        }
        if (Number::class.java.isAssignableFrom(parameter::class.java)) {
            return false
        }
        var param = parameter
        if (parameter.javaClass.name == ArrayList::class.java.name) {
            val list = parameter as List<*>
            if (list.isNotEmpty()) {
                param = list.first()!!
            }
        }
        val idField = Reflects.getIdField(param.javaClass)
        val autoIncrementId = Reflects.isAutoIncrementId(idField)
        return statement.sql.startsWith(INSERT, true) && autoIncrementId
    }

    private fun setIdValue(resultSet: ResultSet, it: Any) {
        val next = resultSet.next()
        if (next.not()) {
            return
        }
        val generatedKey = resultSet.getLong(1)
        val idField = Reflects.getIdField(it.javaClass)
        Reflects.makeAccessible(idField, it)
        idField.set(it, generatedKey)
    }

}
