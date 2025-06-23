package com.tang.kite.io

import com.google.common.base.CaseFormat
import com.tang.kite.utils.Fields
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.lang.reflect.Field

/**
 * @author Tang
 */
object Resources {

    @JvmStatic
    fun getResourceAsStream(name: String): InputStream {
        return Resources::class.java.classLoader.getResourceAsStream(name)
            ?: throw IllegalArgumentException("Resource not found: $name")
    }

    @JvmStatic
    fun getKiteProperties(inputStream: InputStream): Map<String, Map<String, String>> {
        val yaml = Yaml().load<Map<String, Map<String, Map<String, String>>>>(inputStream)
        val kite = yaml["kite"] as Map<String, Map<String, String>>
        return kite
    }

    @JvmStatic
    fun <T> getKiteAsObject(inputStream: InputStream, clazz: Class<T>): T {
        val kiteProperties = getKiteProperties(inputStream)
        return propertyToObject(kiteProperties, clazz)
    }

    @JvmStatic
    fun getDataSourceProperties(inputStream: InputStream): Map<String, String> {
        val kite = getKiteProperties(inputStream)
        val datasource = kite["datasource"] as Map<String, String>
        return datasource
    }

    @JvmStatic
    fun <T> propertyToObject(properties: Map<*, *>, clazz: Class<T>): T {
        val instance = clazz.getDeclaredConstructor().newInstance()
        val fields = clazz.declaredFields + clazz.superclass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val fieldName = getFieldName(properties, field)
            if (fieldName.isEmpty()) {
                continue
            }
            val value = properties[fieldName]
            if (value is Map<*, *>) {
                val nestedObject = propertyToObject(value, field.type)
                field.set(instance, nestedObject)
            } else {
                Fields.setValue(field, instance as Any, value)
            }
        }
        return instance
    }

    private fun getFieldName(properties: Map<*, *>, field: Field): String {
        val fieldName = field.name
        if (properties.containsKey(fieldName)) {
            return fieldName
        } else {
            val hyphenFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fieldName)
            if (properties.containsKey(hyphenFieldName)) {
                return hyphenFieldName
            }
        }
        return ""
    }

}
