package com.tang.kite.config.defaults

import com.tang.kite.config.logical.LogicalDeletionProcessor
import com.tang.kite.config.logical.LogicalDeletionValue
import java.lang.reflect.Field

/**
 * Default implementation of LogicalDeleteProcessor
 *
 * @author Tang
 */
object DefaultLogicalDeletionProcessor : LogicalDeletionProcessor {

    override fun process(field: Field): LogicalDeletionValue {
        return when (field.type) {
            Boolean::class.java, Boolean::class.javaObjectType -> LogicalDeletionValue(normalValue = false, deletedValue = true)
            Byte::class.java, Byte::class.javaObjectType -> LogicalDeletionValue(0.toByte(), 1.toByte())
            Short::class.java, Short::class.javaObjectType -> LogicalDeletionValue(0.toShort(), 1.toShort())
            Int::class.java, Int::class.javaObjectType -> LogicalDeletionValue(0, 1)
            Long::class.java, Long::class.javaObjectType -> LogicalDeletionValue(0L, 1L)
            Char::class.java, Char::class.javaObjectType -> LogicalDeletionValue('0', '1')
            String::class.java -> LogicalDeletionValue("0", "1")
            else -> throw IllegalArgumentException("Unsupported logical deletion type: ${field.type}")
        }
    }

}
