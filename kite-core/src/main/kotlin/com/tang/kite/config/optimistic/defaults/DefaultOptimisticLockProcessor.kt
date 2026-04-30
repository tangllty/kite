package com.tang.kite.config.optimistic.defaults

import com.tang.kite.annotation.optimistic.Version
import com.tang.kite.config.optimistic.OptimisticLockProcessor
import com.tang.kite.exception.UnsupportedTypeException
import com.tang.kite.utils.Reflects
import java.lang.reflect.Field

/**
 * Default implementation of OptimisticLockProcessor
 *
 * @author Tang
 */
class DefaultOptimisticLockProcessor : OptimisticLockProcessor {

    override fun hasVersionField(entity: Any): Boolean {
        return getVersionField(entity) != null
    }

    override fun getVersionField(entity: Any): Field? {
        return entity.javaClass.declaredFields
            .firstOrNull { it.isAnnotationPresent(Version::class.java) }
    }

    override fun getVersionValue(entity: Any): Long? {
        val field = getVersionField(entity) ?: return null

        if (!isSupportedVersionType(field)) {
            throw UnsupportedTypeException("Version field must be of type Long or Int, but found: ${field.type.simpleName}")
        }

        return Reflects.getValue(field, entity)?.let { value ->
            when (value) {
                is Long -> value
                is Int -> value.toLong()
                else -> throw UnsupportedTypeException("Unsupported version type: ${value.javaClass.simpleName}")
            }
        }
    }

    override fun initializeVersion(entity: Any) {
        val field = getVersionField(entity) ?: return

        if (!isSupportedVersionType(field)) {
            throw UnsupportedTypeException("Version field must be of type Long or Int, but found: ${field.type.simpleName}")
        }

        val annotation = field.getAnnotation(Version::class.java)
        val initialValue = annotation.initialValue

        when (field.type) {
            Long::class.java -> Reflects.setValue(field, entity, initialValue)
            Int::class.java -> Reflects.setValue(field, entity, initialValue.toInt())
            else -> throw UnsupportedTypeException("Unsupported version type: ${field.type.simpleName}")
        }
    }

    override fun incrementVersion(entity: Any) {
        val field = getVersionField(entity) ?: return

        if (!isSupportedVersionType(field)) {
            throw UnsupportedTypeException("Version field must be of type Long or Int, but found: ${field.type.simpleName}")
        }

        val currentValue = getVersionValue(entity) ?: 0L
        val newValue = currentValue + 1

        when (field.type) {
            Long::class.java -> Reflects.setValue(field, entity, newValue)
            Int::class.java -> Reflects.setValue(field, entity, newValue.toInt())
            else -> throw UnsupportedTypeException("Unsupported version type: ${field.type.simpleName}")
        }
    }

    override fun isSupportedVersionType(field: Field): Boolean {
        return field.type == Long::class.java || field.type == Int::class.java
    }
}
