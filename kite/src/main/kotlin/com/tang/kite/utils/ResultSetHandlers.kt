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
        val columnCount = metaData.columnCount
        val entity = type.getDeclaredConstructor().newInstance()
        for (i in 1..columnCount) {
            val columnName = metaData.getColumnName(i)
            val field = Reflects.getField(type, columnName)
            if (field == null) {
                continue
            }
            Reflects.makeAccessible(field, entity as Any)
            val columnValue = resultSet.getObject(columnName)
            field.set(entity, columnValue)
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
