package com.tang.jkorm.utils

import com.google.common.base.CaseFormat
import com.tang.jkorm.annotation.Column
import com.tang.jkorm.annotation.Table
import com.tang.jkorm.annotation.id.Id
import com.tang.jkorm.annotation.id.IdType
import com.tang.jkorm.function.SFunction
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * @author Tang
 */
object Reflects {

    private val idFieldCache: ConcurrentMap<Class<*>, Field> = ConcurrentHashMap()

    private val fieldCache: ConcurrentMap<Pair<Class<*>, String>, Field> = ConcurrentHashMap()

    private val columnNameCache: ConcurrentMap<Field, String> = ConcurrentHashMap()

    fun makeAccessible(accessibleObject: AccessibleObject, any: Any) {
        if (accessibleObject.canAccess(any)) {
            return
        }
        accessibleObject.trySetAccessible()
    }

    fun getIdField(clazz: Class<*>): Field {
        return idFieldCache.computeIfAbsent(clazz) {
            val fields = clazz.declaredFields.filter { it.isAnnotationPresent(Id::class.java) }
            if (fields.size > 1) {
                throw IllegalArgumentException("More than one @Id field found in ${clazz.simpleName}, found: ${fields.joinToString { it.name }}")
            }
            if (fields.isEmpty()) {
                throw IllegalArgumentException("No @Id field found in ${clazz.simpleName}")
            }
            fields.first()
        }
    }

    fun isAutoIncrementId(clazz: Class<*>): Boolean {
        val idField = getIdField(clazz)
        return isAutoIncrementId(idField)
    }

    fun isAutoIncrementId(idField: Field): Boolean {
        return idField.getAnnotation(Id::class.java).type == IdType.AUTO
    }

    fun getTableName(clazz: Class<*>): String {
        val table = clazz.getAnnotation(Table::class.java)
        return table?.value ?: clazz.simpleName.lowercase()
    }

    fun getField(clazz: Class<*>, columnName: String): Field {
        val cacheKey = Pair(clazz, columnName)
        return fieldCache.computeIfAbsent(cacheKey) {
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
                }?.let { return@computeIfAbsent it }
                return@computeIfAbsent field ?: throw IllegalArgumentException("No field found for column $columnName in ${clazz.simpleName}")
            }
            fields.first()
        }
    }

    fun getColumnName(field: Field): String {
        return columnNameCache.computeIfAbsent(field) {
            if (field.isAnnotationPresent(Column::class.java) && field.getAnnotation(Column::class.java).value.isNotBlank()) {
                field.getAnnotation(Column::class.java).value
            } else {
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.name)
            }
        }
    }

    fun <T> getColumnName(column: KMutableProperty1<T, *>): String {
        var filed = column.javaField!!
        return getColumnName(filed)
    }

    fun <T> getColumnName(column: SFunction<T, *>): String {
        val filed = Fields.getField(column)
        return getColumnName(filed)
    }

    fun getGeneratedId(clazz: Class<*>): Any {
        val idField = getIdField(clazz)
        return getGeneratedId(idField)
    }

    fun getGeneratedId(idField: Field): Any {
        val idStrategy = idField.getAnnotation(Id::class.java).idStrategy
        return idStrategy.java.getDeclaredConstructor().newInstance().getId(idField)
    }

    fun getSqlFields(clazz: Class<*>): List<Field> {
        return clazz.declaredFields.filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

}
