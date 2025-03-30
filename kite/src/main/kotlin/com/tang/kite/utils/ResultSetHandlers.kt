package com.tang.kite.utils

import com.tang.kite.constants.SqlString.INSERT
import com.tang.kite.sql.SqlStatement
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
        val metaDataMap = mutableMapOf<String, MutableList<Pair<String, Any>>>()
        val entity = type.getDeclaredConstructor().newInstance()
        val tableName = Reflects.getTableName(type)
        for (i in 1..metaData.columnCount) {
            val columnTableName = metaData.getTableName(i).lowercase()
            val columnName = metaData.getColumnName(i)
            val columnValue = resultSet.getObject(i)
            if (metaDataMap.containsKey(columnTableName).not()) {
                metaDataMap[columnTableName] = mutableListOf()
            }
            metaDataMap[columnTableName]!!.add(Pair(columnName, columnValue))
        }
        val metaDataList = metaDataMap[tableName] ?: return entity
        metaDataList.forEach { (columnName, columnValue) ->
            val field = Reflects.getField(type, columnName) ?: return@forEach
            Reflects.makeAccessible(field, entity as Any)
            field.set(entity, columnValue)
        }
        val joins = Reflects.getJoins(type)
        if (joins.isEmpty()) {
            return entity
        }
        joins.forEach {
            val joinTableName = Reflects.getTableName(it.type)
            val joinEntity = it.type.getDeclaredConstructor().newInstance()
            val joinMetaData = metaDataMap[joinTableName] ?: return@forEach
            joinMetaData.forEach metaData@ { (columnName, columnValue) ->
                val field = Reflects.getField(it.type, columnName) ?: return@metaData
                Reflects.makeAccessible(field, joinEntity as Any)
                field.set(joinEntity, columnValue)
            }
            Reflects.makeAccessible(it, entity as Any)
            it.set(entity, joinEntity)
        }
        return entity
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
        if (parameter.javaClass.name in listOf(UpdateWrapper::class.java.name, Long::class.javaObjectType.name)) {
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
