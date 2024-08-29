package com.tang.jkorm.utils

import com.google.common.base.CaseFormat
import com.tang.jkorm.annotation.Column
import com.tang.jkorm.annotation.Id
import com.tang.jkorm.annotation.Table
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field

/**
 * @author Tang
 */
object Reflects {

    fun makeAccessible(accessibleObject: AccessibleObject, any: Any) {
        if (accessibleObject.canAccess(any)) {
            return
        }
        accessibleObject.trySetAccessible()
    }

    fun getIdField(clazz: Class<*>): Field {
        val fields = clazz.declaredFields.filter { it.isAnnotationPresent(Id::class.java) }
        if (fields.size > 1) {
            throw IllegalArgumentException("More than one @Id field found in ${clazz.simpleName}" +
                ", found: ${fields.joinToString { it.name }}")
        }
        if (fields.isEmpty()) {
            throw IllegalArgumentException("No @Id field found in ${clazz.simpleName}")
        }
        return fields.first()
    }

    fun isAutoIncrementId(clazz: Class<*>): Boolean {
        val idField = getIdField(clazz)
        return idField.getAnnotation(Id::class.java).autoIncrement
    }

    fun getTableName(clazz: Class<*>): String {
        val table = clazz.getAnnotation(Table::class.java)
        return table?.value ?: clazz.simpleName.lowercase()
    }

    fun getField(clazz: Class<*>, columnName: String): Field {
        val lowerColumnName = columnName.lowercase()
        val lowerCamelColumnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, lowerColumnName)
        val fields = clazz.declaredFields.filter {
            it.isAnnotationPresent(Column::class.java) && it.getAnnotation(Column::class.java).value.lowercase() == lowerColumnName
        }
        if (fields.size > 1) {
            throw IllegalArgumentException("More than one field found for column $columnName in ${clazz.simpleName}" +
                ", found: ${fields.joinToString { it.name }}")
        }
        if (fields.isEmpty()) {
            val field = clazz.declaredFields.firstOrNull {
                it.name == lowerCamelColumnName
            }?.let { return it }
            return field ?: throw IllegalArgumentException("No field found for column $columnName in ${clazz.simpleName}")
        }
        return fields.first()
    }

    fun getColumnName(field: Field): String {
        if (field.isAnnotationPresent(Column::class.java)) {
            return field.getAnnotation(Column::class.java).value
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.name)
    }

}
