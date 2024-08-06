package com.tang.jkorm.utils

import com.tang.jkorm.annotation.Id
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field

/**
 * @author Tang
 */
object Reflects {

    fun makeAccessible(accessibleObject: AccessibleObject, any: Any) {
        if (!accessibleObject.isAccessible) {
            accessibleObject.isAccessible = true
        }
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
        val table = clazz.getAnnotation(com.tang.jkorm.annotation.Table::class.java)
        return table?.value ?: clazz.simpleName.lowercase()
    }

}
