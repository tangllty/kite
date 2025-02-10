package com.tang.kite.utils

import java.sql.PreparedStatement

/**
 * Statement utils
 *
 * @author Tang
 */
object Statements {

    fun setValues(statement: PreparedStatement, parameters: List<Any?>) {
        parameters.forEachIndexed { index, parameter ->
            setValue(statement, index + 1, parameter)
        }
    }

    fun setValue(statement: PreparedStatement, index: Int, parameter: Any?) {
        when (parameter) {
            null -> statement.setObject(index, null)
            is Int -> statement.setInt(index, parameter)
            is Long -> statement.setLong(index, parameter)
            is String -> statement.setString(index, parameter)
            is Boolean -> statement.setBoolean(index, parameter)
            is Byte -> statement.setByte(index, parameter)
            is Short -> statement.setShort(index, parameter)
            is Float -> statement.setFloat(index, parameter)
            is Double -> statement.setDouble(index, parameter)
            is ByteArray -> statement.setBytes(index, parameter)
            else -> statement.setObject(index, parameter)
        }
    }

}
