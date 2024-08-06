package com.tang.jkorm.utils

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
            val field = type.getDeclaredField(columnName.lowercase())
            field.isAccessible = true
            val columnValue = resultSet.getObject(columnName)
            field.set(entity, columnValue)
        }
        return entity
    }

}
