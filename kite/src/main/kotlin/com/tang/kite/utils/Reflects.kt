package com.tang.kite.utils

import com.google.common.base.CaseFormat
import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Join
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.function.SFunction
import com.tang.kite.logging.LOGGER
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

/**
 * Reflection utility class
 *
 * @author Tang
 */
object Reflects {

    private val idFieldCache: ConcurrentMap<Class<*>, Field> = ConcurrentHashMap()

    private val fieldCache: ConcurrentMap<Pair<Class<*>, String>, Field> = ConcurrentHashMap()

    private val columnNameCache: ConcurrentMap<Field, String> = ConcurrentHashMap()

    private val tableNameCache: ConcurrentMap<Class<*>, String> = ConcurrentHashMap()

    private val tableAliasCache: ConcurrentMap<Class<*>, String> = ConcurrentHashMap()

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
        return tableNameCache.computeIfAbsent(clazz) {
            if (clazz.isAnnotationPresent(Table::class.java) && clazz.getAnnotation(Table::class.java).value.isNotBlank()) {
                clazz.getAnnotation(Table::class.java).value
            } else {
                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.simpleName)
            }
        }
    }

    fun getField(clazz: Class<*>, columnName: String): Field? {
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
                LOGGER.warn("No field found for column $columnName in ${clazz.simpleName}")
                return@computeIfAbsent field
            }
            fields.first()
        }
    }

    /**
     * Get the column name, if [Column.value] not set, convert the field name to lower underscore
     *
     * @param field field
     * @param withAlias with table alias
     * @return column name
     */
    fun getColumnName(field: Field, withAlias: Boolean = false): String {
        val columnName = columnNameCache.computeIfAbsent(field) {
            if (field.isAnnotationPresent(Column::class.java) && field.getAnnotation(Column::class.java).value.isNotBlank()) {
                field.getAnnotation(Column::class.java).value
            } else {
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.name)
            }
        }
        if (withAlias) {
            val alias = getTableAlias(field.declaringClass)
            return "${alias}.${columnName}"
        } else {
            return columnName
        }
    }

    fun <T> getColumnName(column: KMutableProperty1<T, *>, withAlias: Boolean = false): String {
        var field = column.javaField!!
        return getColumnName(field, withAlias)
    }

    fun <T> getColumnName(column: SFunction<T, *>, withAlias: Boolean = false): String {
        val field = Fields.getField(column)
        return getColumnName(field, withAlias)
    }

    fun getGeneratedId(clazz: Class<*>): Any {
        val idField = getIdField(clazz)
        return getGeneratedId(idField)
    }

    fun getGeneratedId(idField: Field): Any {
        val idStrategy = idField.getAnnotation(Id::class.java).idStrategy
        return idStrategy.java.getDeclaredConstructor().newInstance().getId(idField)
    }

    /**
     * Get all fields without `Join` annotation and [Column.ignore] set to false
     *
     * @param clazz entity class
     * @return fields
     */
    fun getSqlFields(clazz: Class<*>): List<Field> {
        return clazz.declaredFields
            .filter { it.isAnnotationPresent(Join::class.java).not() }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    /**
     * Get all fields with `Join` annotation and [Column.ignore] set to false
     * If the field type is [Iterable], it will be ignored
     *
     * @param clazz entity class
     * @return fields
     */
    fun getJoins(clazz: Class<*>): List<Field> {
        return clazz.declaredFields
            .filter { it.isAnnotationPresent(Join::class.java) }
            .filter { Iterable::class.java.isAssignableFrom(it.type).not() }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    fun getIterableJoins(clazz: Class<*>): List<Field> {
        return clazz.declaredFields
            .filter { it.isAnnotationPresent(Join::class.java) }
            .filter { Iterable::class.java.isAssignableFrom(it.type) }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    /**
     * Get the table alias, if [Table.alias] not set, use the first letter of each word in the table name
     *
     * @param clazz entity class
     * @return table alias
     */
    fun getTableAlias(clazz: Class<*>): String {
        return tableAliasCache.computeIfAbsent(clazz) {
            if (clazz.isAnnotationPresent(Table::class.java) && clazz.getAnnotation(Table::class.java).alias.isNotBlank()) {
                clazz.getAnnotation(Table::class.java).alias
            } else {
                val tableName = getTableName(clazz)
                tableName.split("_").joinToString("") { it.first().toString() }
            }
        }
    }

}
