package com.tang.kite.utils

import com.google.common.base.CaseFormat
import com.tang.kite.annotation.Column
import com.tang.kite.annotation.Join
import com.tang.kite.annotation.Table
import com.tang.kite.annotation.id.Id
import com.tang.kite.annotation.id.IdType
import com.tang.kite.config.KiteConfig
import com.tang.kite.constants.SqlString.DOT
import com.tang.kite.enumeration.SqlType
import com.tang.kite.function.SFunction
import com.tang.kite.handler.fill.Fill
import com.tang.kite.handler.fill.FillKey
import com.tang.kite.logging.LOGGER
import com.tang.kite.result.ResultHandlerFactory
import java.lang.invoke.SerializedLambda
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.containsKey
import kotlin.collections.get
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

/**
 * Reflection utility class
 *
 * @author Tang
 */
object Reflects {

    private val tableNameCache: ConcurrentMap<Class<*>, String> = ConcurrentHashMap()

    private val tableAliasCache: ConcurrentMap<Class<*>, String> = ConcurrentHashMap()

    private val fieldsCache: ConcurrentMap<Class<*>, List<Field>> = ConcurrentHashMap()

    private val joinFieldsCache: ConcurrentMap<Class<*>, List<Field>> = ConcurrentHashMap()

    private val idFieldCache: ConcurrentMap<Class<*>, Field> = ConcurrentHashMap()

    private val fieldCache: ConcurrentMap<Pair<Class<*>, String>, Field> = ConcurrentHashMap()

    private val functionSerializedLambdaCache: ConcurrentMap<SFunction<*, *>, SerializedLambda> = ConcurrentHashMap()

    private val functionFieldNameCache: ConcurrentMap<SFunction<*, *>, String> = ConcurrentHashMap()

    private val functionFieldCache: ConcurrentMap<SFunction<*, *>, Field> = ConcurrentHashMap()

    private val columnNameCache: ConcurrentMap<Field, String> = ConcurrentHashMap()

    @JvmStatic
    fun <T> makeAccessible(accessibleObject: AccessibleObject, instance: T) {
        if (accessibleObject.canAccess(instance)) {
            return
        }
        accessibleObject.trySetAccessible()
    }

    @JvmStatic
    fun getTableName(clazz: Class<*>): String {
        return tableNameCache.computeIfAbsent(clazz) {
            if (clazz.isAnnotationPresent(Table::class.java) && clazz.getAnnotation(Table::class.java).value.isNotBlank()) {
                clazz.getAnnotation(Table::class.java).value
            } else {
                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.simpleName)
            }
        }
    }

    /**
     * Get the table alias, if [Table.alias] not set, use the first letter of each word in the table name
     *
     * @param clazz entity class
     * @return table alias
     */
    @JvmStatic
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

    @JvmStatic
    fun getFields(clazz: Class<*>): List<Field> {
        return fieldsCache.computeIfAbsent(clazz) {
            val declaredFields = clazz.declaredFields.toList()
            val superDeclaredFields = clazz.superclass.declaredFields.toList()
            val fields = declaredFields + superDeclaredFields
            fields.filter { it.name != "serialVersionUID" }
        }
    }

    /**
     * Get all fields without `Join` annotation and [Column.ignore] set to false
     *
     * @param clazz entity class
     * @return fields
     */
    @JvmStatic
    fun getSqlFields(clazz: Class<*>): List<Field> {
        return getFields(clazz)
            .filter { it.isAnnotationPresent(Join::class.java).not() }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    /**
     * Get all fields with `Join` annotation
     *
     * @param clazz entity class
     * @return fields
     */
    private fun getJoinFields(clazz: Class<*>): List<Field> {
        return joinFieldsCache.computeIfAbsent(clazz) {
            getFields(clazz).filter { it.isAnnotationPresent(Join::class.java) }
        }
    }

    /**
     * Get all fields with `Join` annotation and [Column.ignore] set to false
     * If the field type is [Iterable], it will be ignored
     *
     * @param clazz entity class
     * @return fields
     */
    @JvmStatic
    fun getJoins(clazz: Class<*>): List<Field> {
        return getJoinFields(clazz)
            .filter { Iterable::class.java.isAssignableFrom(it.type).not() }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    /**
     * Get all fields with `Join` annotation and [Column.ignore] set to false
     * If the field type is [Iterable], it will be included
     *
     * @param clazz entity class
     * @return fields
     */
    @JvmStatic
    fun getIterableJoins(clazz: Class<*>): List<Field> {
        return getJoinFields(clazz)
            .filter { Iterable::class.java.isAssignableFrom(it.type) }
            .filter { it.isAnnotationPresent(Column::class.java).not() || it.getAnnotation(Column::class.java).ignore.not() }
    }

    @JvmStatic
    fun getIdField(clazz: Class<*>): Field {
        return idFieldCache.computeIfAbsent(clazz) {
            val fields = getFields(clazz).filter { it.isAnnotationPresent(Id::class.java) }
            if (fields.size > 1) {
                throw IllegalArgumentException("More than one @Id field found in ${clazz.simpleName}, found: ${fields.joinToString { it.name }}")
            }
            if (fields.isEmpty()) {
                throw IllegalArgumentException("No @Id field found in ${clazz.simpleName}")
            }
            fields.first()
        }
    }

    @JvmStatic
    fun isAutoIncrementId(clazz: Class<*>): Boolean {
        val idField = getIdField(clazz)
        return isAutoIncrementId(idField)
    }

    @JvmStatic
    fun isAutoIncrementId(idField: Field): Boolean {
        return idField.getAnnotation(Id::class.java).type == IdType.AUTO
    }

    @JvmStatic
    fun getGeneratedId(clazz: Class<*>): Any {
        val idField = getIdField(clazz)
        return getGeneratedId(idField)
    }

    @JvmStatic
    fun getGeneratedId(idField: Field): Any {
        val idStrategy = idField.getAnnotation(Id::class.java).idStrategy
        return idStrategy.java.getDeclaredConstructor().newInstance().getId(idField)
    }

    @JvmStatic
    fun getField(clazz: Class<*>, columnName: String): Field? {
        val cacheKey = Pair(clazz, columnName)
        return fieldCache.computeIfAbsent(cacheKey) {
            val lowerColumnName = columnName.lowercase()
            val lowerCamelColumnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, lowerColumnName)
            val fieldList = getFields(clazz)
            val fields = fieldList.filter {
                it.isAnnotationPresent(Column::class.java) && it.getAnnotation(Column::class.java).value.lowercase() == lowerColumnName
            }
            if (fields.size > 1) {
                throw IllegalArgumentException("More than one field found for column $columnName in ${clazz.simpleName}" +
                    ", found: ${fields.joinToString { it.name }}")
            }
            if (fields.isEmpty()) {
                val field = fieldList.firstOrNull {
                    it.name == lowerCamelColumnName
                }?.let { return@computeIfAbsent it }
                LOGGER.warn("No field found for column $columnName in ${clazz.simpleName}")
                return@computeIfAbsent field
            }
            fields.first()
        }
    }

    private fun <T> getSerializedLambda(function: SFunction<T, *>): SerializedLambda {
        return functionSerializedLambdaCache.computeIfAbsent(function) {
            val method = function.javaClass.getDeclaredMethod("writeReplace")
            makeAccessible(method, function)
            method.invoke(function) as SerializedLambda
        }
    }

    @JvmStatic
    fun <T> getFieldName(function: SFunction<T, *>): String {
        return functionFieldNameCache.computeIfAbsent(function) {
            val serializedLambda = getSerializedLambda(function)
            val methodPrefix = "get"
            val fieldName = serializedLambda.implMethodName.substring(methodPrefix.length)
            fieldName.replaceFirst(
                fieldName[0].toString().toRegex(),
                fieldName[0].toString().lowercase(Locale.getDefault())
            )
        }
    }

    @JvmStatic
    fun <T> getFieldName(property: KProperty1<T, *>): String {
        return property.name
    }

    @JvmStatic
    fun <T> getField(function: SFunction<T, *>): Field {
        return functionFieldCache.computeIfAbsent(function) {
            val fieldName = getFieldName(function)
            val serializedLambda = getSerializedLambda(function)
            Class.forName(serializedLambda.implClass.replace("/", ".")).getDeclaredField(fieldName)
        }
    }

    @JvmStatic
    fun <T> getField(property: KProperty1<T, *>): Field {
        val fieldName = getFieldName(property)
        return property.javaField ?: throw NoSuchFieldException("Field not found: $fieldName")
    }

    /**
     * Get the column name, if [Column.value] not set, convert the field name to lower underscore
     *
     * @param field field
     * @param withAlias with table alias
     * @return column name
     */
    @JvmStatic
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

    @JvmStatic
    fun <T> getColumnName(column: KMutableProperty1<T, *>, withAlias: Boolean = false): String {
        val field = column.javaField!!
        return getColumnName(field, withAlias)
    }

    @JvmStatic
    fun <T> getColumnName(column: SFunction<T, *>, withAlias: Boolean = false): String {
        val field = getField(column)
        return getColumnName(field, withAlias)
    }

    /**
     * Set the field value, if the field type is Long and the value is Int, it will be automatically converted to Long
     *
     * @param field the field to set
     * @param target the target object to set the field on
     * @param value the value to set, can be null
     */
    @JvmStatic
    fun <T> setValue(field: Field, target: T, value: Any?) {
        makeAccessible(field, target)
        if (value != null && field.type == java.lang.Long::class.java && value is Int) {
            field.set(target, value.toLong())
        } else {
            field.set(target, value)
        }
    }



    @JvmStatic
    fun <T> setResultValue(field: Field, instance: T, value: Any?) {
        makeAccessible(field, instance)
        val resultHandler = ResultHandlerFactory().newResultHandler(field)
        if (value == null) {
            resultHandler.setNullValue(field, instance, value)
        } else {
            resultHandler.setValue(field, instance, value)
        }
    }

    @JvmStatic
    fun <T> getValue(field: Field, entity: T): Any? {
        makeAccessible(field, entity)
        return field.get(entity)
    }

    @JvmStatic
    fun <T> getFill(field: Field, entity: T, sqlType: SqlType): Fill? {
        makeAccessible(field, entity)
        val fillAnnotationHandlers = KiteConfig.fillHandlers
        val annotations = field.annotations
        for (annotation in annotations) {
            val key = FillKey(annotation.annotationClass, sqlType)
            val handler = fillAnnotationHandlers[key]
            if (handler != null) {
                return Fill(annotation, handler, sqlType)
            }
        }
        return null
    }

    @JvmStatic
    fun <T> isFillField(field: Field, entity: T, sqlType: SqlType): Boolean {
        return getFill(field, entity, sqlType) != null
    }

    @JvmStatic
    fun <T> getValue(field: Field, entity: T, sqlType: SqlType): Any? {
        val fill = getFill(field, entity, sqlType)
        return if (fill != null) {
            fill.handler.fillValue(fill.annotation, field, entity as Any)
        } else {
            getValue(field, entity)
        }
    }

    @JvmStatic
    fun <T> setTableFillFields(tableClass: Class<T>?, sqlType: SqlType, action: (String, Any?) -> Unit) {
        if (tableClass == null) {
            throw IllegalArgumentException("Table class is not set")
        }
        val fields = getSqlFields(tableClass)
        val entity = tableClass.getDeclaredConstructor().newInstance()
        fields.forEach {
            makeAccessible(it, entity)
            if (isFillField(it, entity, sqlType).not()) {
                return@forEach
            }
            action(getColumnName(it), getValue(it, entity, sqlType))
        }
    }

    /**
     * Get the value of a field from an object or a map.
     *
     * @param any The object or map from which to retrpieve the value.
     * @param param The field name or a dot-separated ath to the field.
     * @return The value of the field, or null if not found.
     */
    @JvmStatic
    fun getValue(any: Any, param: String): Any? {
        val parts = param.split(DOT)
        var current: Any? = any
        for (part in parts) {
            current = when (current) {
                is Map<*, *> -> {
                    if (current.containsKey(part)) {
                        current[part]
                    } else {
                        throw NoSuchFieldException("Key not found: $part in map")
                    }
                }
                else -> {
                    val field = generateSequence(current?.javaClass) { it.superclass }
                        .flatMap { it.declaredFields.asSequence() }
                        .find { it.name == part }
                        ?: throw NoSuchFieldException("Field not found: $part in ${current?.javaClass}")
                    getValue(field, current)
                }
            }
            if (current == null) break
        }
        return current
    }

}
